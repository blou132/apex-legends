#include <asm/ptrace.h>
#include <errno.h>
#include <inttypes.h>
#include <linux/elf.h>
#include <signal.h>
#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/uio.h>
#include <sys/wait.h>

#ifndef NT_ARM_HW_BREAK
#define NT_ARM_HW_BREAK 0x402
#endif

#define ARM_MAX_BRP 16U
#define ARM_BREAKPOINT_LEN_4 0x0fU
#define ARM_BREAKPOINT_EXECUTE 0U
#define AARCH64_BREAKPOINT_EL0 2U

#define HW_EXEC_CTRL ((ARM_BREAKPOINT_LEN_4 << 5) | \
                      (ARM_BREAKPOINT_EXECUTE << 3) | \
                      (AARCH64_BREAKPOINT_EL0 << 1) | 1U)

/* Disposable targets only: an unverified restore terminates the tracee. */

struct hwdebug_register {
    uint64_t address;
    uint32_t control;
    uint32_t pad;
};

struct user_hwdebug_state_compat {
    uint32_t debug_info;
    uint32_t pad;
    struct hwdebug_register debug_registers[ARM_MAX_BRP];
};

_Static_assert(sizeof(struct hwdebug_register) == 16U, "unexpected hwdebug register size");
_Static_assert(
    sizeof(struct user_hwdebug_state_compat) == 264U,
    "unexpected hwdebug state size");
_Static_assert(
    offsetof(struct user_hwdebug_state_compat, debug_registers) == 8U,
    "unexpected hwdebug register offset");

static const uint64_t expected_arguments[8] = {
    UINT64_C(0x1111),
    UINT64_C(0x2222),
    UINT64_C(0x3333),
    UINT64_C(0x4444),
    UINT64_C(0x5555),
    UINT64_C(0x6666),
    UINT64_C(0x7777),
    UINT64_C(0x8888),
};

static void print_errno(const char *operation) {
    printf("%s=FAILURE errno=%d message=%s\n", operation, errno, strerror(errno));
}

static int parse_positive_pid(const char *text, pid_t *result) {
    char *end = NULL;
    long value;

    errno = 0;
    value = strtol(text, &end, 10);
    if (errno != 0 || end == text || *end != '\0' || value <= 0) {
        return -1;
    }

    *result = (pid_t) value;
    return 0;
}

static int parse_uint64(const char *text, uint64_t *result) {
    char *end = NULL;
    unsigned long long value;

    errno = 0;
    value = strtoull(text, &end, 0);
    if (errno != 0 || end == text || *end != '\0') {
        return -1;
    }

    *result = (uint64_t) value;
    return 0;
}

static int get_registers(pid_t pid, struct user_pt_regs *registers, size_t *length) {
    struct iovec register_set = {
        .iov_base = registers,
        .iov_len = sizeof(*registers),
    };

    memset(registers, 0, sizeof(*registers));
    if (ptrace(PTRACE_GETREGSET, pid, (void *) NT_PRSTATUS, &register_set) == -1) {
        return -1;
    }

    *length = register_set.iov_len;
    return 0;
}

static int get_hw_break_state(
        pid_t pid,
        struct user_hwdebug_state_compat *state,
        size_t *length) {
    struct iovec register_set = {
        .iov_base = state,
        .iov_len = sizeof(*state),
    };

    memset(state, 0, sizeof(*state));
    if (ptrace(PTRACE_GETREGSET, pid, (void *) NT_ARM_HW_BREAK, &register_set) == -1) {
        return -1;
    }

    *length = register_set.iov_len;
    return 0;
}

static int set_first_hw_break_slot(pid_t pid, uint64_t address, uint32_t control) {
    struct user_hwdebug_state_compat state;
    struct iovec register_set;

    memset(&state, 0, sizeof(state));
    state.debug_registers[0].address = address;
    state.debug_registers[0].control = control;

    register_set.iov_base = &state;
    register_set.iov_len = offsetof(
        struct user_hwdebug_state_compat, debug_registers[1]);

    return ptrace(PTRACE_SETREGSET, pid, (void *) NT_ARM_HW_BREAK, &register_set);
}

static int restore_first_hw_break_slot(
        pid_t pid,
        const struct hwdebug_register *initial_register,
        size_t expected_length) {
    struct user_hwdebug_state_compat state;
    size_t length = 0;
    uint64_t current_address;

    if (get_hw_break_state(pid, &state, &length) == -1 ||
            length != expected_length) {
        return -1;
    }
    current_address = state.debug_registers[0].address;

    if (set_first_hw_break_slot(pid, current_address, 0U) == -1 ||
            get_hw_break_state(pid, &state, &length) == -1 ||
            length != expected_length ||
            state.debug_registers[0].control != 0U) {
        errno = EIO;
        return -1;
    }

    if (set_first_hw_break_slot(
            pid, initial_register->address, initial_register->control) == -1 ||
            get_hw_break_state(pid, &state, &length) == -1 ||
            length != expected_length ||
            state.debug_registers[0].address != initial_register->address ||
            state.debug_registers[0].control != initial_register->control) {
        errno = EIO;
        return -1;
    }

    return 0;
}

static void terminate_stopped_tracee(pid_t pid) {
    int status;

    if (ptrace(PTRACE_KILL, pid, NULL, NULL) == -1) {
        kill(pid, SIGKILL);
    }
    (void) waitpid(pid, &status, 0);
    puts("TRACEE_TERMINATED_FOR_SAFETY=YES");
}

int main(int argc, char **argv) {
    pid_t target_pid;
    uint64_t breakpoint_address;
    struct user_hwdebug_state_compat initial_hw_state;
    struct user_hwdebug_state_compat readback_hw_state;
    struct user_pt_regs trap_registers;
    size_t initial_hw_length = 0;
    size_t readback_hw_length = 0;
    size_t register_length = 0;
    siginfo_t signal_info;
    unsigned int slot_count;
    unsigned int debug_arch;
    unsigned int index;
    int status = 0;
    int attached = 0;
    int target_stopped = 0;
    int hw_state_dirty = 0;
    int result = 1;

    if (argc != 3) {
        fprintf(stderr, "usage: ptrace_hw_breakpoint_probe <pid> <address>\n");
        return 2;
    }
    if (parse_positive_pid(argv[1], &target_pid) != 0 ||
            parse_uint64(argv[2], &breakpoint_address) != 0 ||
            (breakpoint_address & UINT64_C(3)) != 0U) {
        fprintf(stderr, "invalid argument\n");
        return 2;
    }

    if (ptrace(PTRACE_ATTACH, target_pid, NULL, NULL) == -1) {
        print_errno("PTRACE_ATTACH_RESULT");
        return 1;
    }
    attached = 1;
    puts("PTRACE_ATTACH_RESULT=SUCCESS");

    if (waitpid(target_pid, &status, 0) == -1 || !WIFSTOPPED(status)) {
        if (errno != 0) {
            print_errno("PTRACE_WAIT_STOP_RESULT");
        } else {
            printf("PTRACE_WAIT_STOP_RESULT=FAILURE status=%d\n", status);
        }
        goto cleanup;
    }
    target_stopped = 1;
    printf("PTRACE_WAIT_STOP_RESULT=SUCCESS signal=%d\n", WSTOPSIG(status));

    if (get_hw_break_state(target_pid, &initial_hw_state, &initial_hw_length) == -1) {
        print_errno("ARM64_HW_BREAK_REGSET_SUPPORTED");
        goto cleanup;
    }
    puts("ARM64_HW_BREAK_REGSET_SUPPORTED=YES");
    printf("HW_BREAK_REGSET_LENGTH=%zu\n", initial_hw_length);
    if (initial_hw_length < offsetof(struct user_hwdebug_state_compat, debug_registers) ||
            initial_hw_length > sizeof(initial_hw_state)) {
        puts("HW_BREAK_REGSET_LAYOUT_VALID=NO");
        goto cleanup;
    }
    puts("HW_BREAK_REGSET_LAYOUT_VALID=YES");

    slot_count = initial_hw_state.debug_info & UINT32_C(0xff);
    debug_arch = (initial_hw_state.debug_info >> 8) & UINT32_C(0xff);
    printf("HW_EXEC_BREAKPOINT_SLOTS=%u\n", slot_count);
    printf("HW_DEBUG_ARCH_VERSION=%u\n", debug_arch);
    if (slot_count < 1U || slot_count > ARM_MAX_BRP) {
        puts("HW_BREAKPOINT_RESOURCE_VALID=NO");
        goto cleanup;
    }
    puts("HW_BREAKPOINT_RESOURCE_VALID=YES");

    for (index = 0; index < slot_count; index++) {
        if (initial_hw_state.debug_registers[index].control != 0U) {
            puts("HW_BREAKPOINT_INITIAL_STATE_CLEAR=NO");
            goto cleanup;
        }
    }
    puts("HW_BREAKPOINT_INITIAL_STATE_CLEAR=YES");

    hw_state_dirty = 1;
    if (set_first_hw_break_slot(target_pid, breakpoint_address, HW_EXEC_CTRL) == -1) {
        print_errno("HW_BREAKPOINT_SETREGSET");
        goto cleanup;
    }
    puts("HW_BREAKPOINT_SETREGSET=SUCCESS");

    if (get_hw_break_state(target_pid, &readback_hw_state, &readback_hw_length) == -1) {
        print_errno("HW_BREAKPOINT_READBACK");
        goto cleanup;
    }
    printf("HW_BREAKPOINT_READBACK_LENGTH=%zu\n", readback_hw_length);
    printf("HW_BREAKPOINT_ADDRESS_MATCH=%s\n",
           readback_hw_state.debug_registers[0].address == breakpoint_address ?
               "YES" : "NO");
    printf("HW_BREAKPOINT_CONTROL_EXPECTED=0x%08" PRIx32 "\n", HW_EXEC_CTRL);
    printf("HW_BREAKPOINT_CONTROL_OBSERVED=0x%08" PRIx32 "\n",
           readback_hw_state.debug_registers[0].control);
    if (readback_hw_length != initial_hw_length ||
            readback_hw_state.debug_registers[0].address != breakpoint_address ||
            readback_hw_state.debug_registers[0].control != HW_EXEC_CTRL) {
        puts("HW_BREAKPOINT_READBACK_VALID=NO");
        goto cleanup;
    }
    puts("HW_BREAKPOINT_READBACK_VALID=YES");

    if (ptrace(PTRACE_CONT, target_pid, NULL, NULL) == -1) {
        print_errno("PTRACE_CONT_RESULT");
        goto cleanup;
    }
    target_stopped = 0;
    if (waitpid(target_pid, &status, 0) == -1) {
        print_errno("HW_BREAKPOINT_WAIT_RESULT");
        goto cleanup;
    }
    if (!WIFSTOPPED(status)) {
        printf("HW_BREAKPOINT_WAIT_RESULT=FAILURE status=%d\n", status);
        attached = 0;
        goto cleanup;
    }
    target_stopped = 1;
    printf("HW_BREAKPOINT_STOP_SIGNAL=%d\n", WSTOPSIG(status));
    if (WSTOPSIG(status) != SIGTRAP) {
        puts("HW_BREAKPOINT_SIGTRAP=NO");
        goto cleanup;
    }
    puts("HW_BREAKPOINT_SIGTRAP=YES");

    memset(&signal_info, 0, sizeof(signal_info));
    if (ptrace(PTRACE_GETSIGINFO, target_pid, NULL, &signal_info) == -1) {
        print_errno("HW_SIGTRAP_INFO_CAPTURED");
        goto cleanup;
    }
    printf("HW_SIGTRAP_INFO_CAPTURED=YES signo=%d code=%d\n",
           signal_info.si_signo, signal_info.si_code);
    printf("HW_SIGTRAP_ADDRESS_MATCH=%s\n",
           (uintptr_t) signal_info.si_addr == breakpoint_address ? "YES" : "NO");

    if (get_registers(target_pid, &trap_registers, &register_length) == -1) {
        print_errno("HW_TRAP_GETREGSET_RESULT");
        goto cleanup;
    }
    printf("REGISTER_SET_LENGTH=%zu\n", register_length);
    if (register_length != sizeof(trap_registers)) {
        puts("HW_TRAP_REGISTER_LAYOUT_VALID=NO");
        goto cleanup;
    }
    puts("HW_TRAP_REGISTER_LAYOUT_VALID=YES");
    printf("HW_PC_AT_FUNCTION_ENTRY=%s\n",
           trap_registers.pc == breakpoint_address ? "YES" : "NO");
    if (trap_registers.pc != breakpoint_address) {
        goto cleanup;
    }
    puts("PC_CAPTURED=YES");
    puts("SP_CAPTURED=YES");
    puts("LR_CAPTURED=YES");
    puts("FP_CAPTURED=YES");

    for (index = 0; index < 8U; index++) {
        printf("X%u_MATCH=%s\n", index,
               trap_registers.regs[index] == expected_arguments[index] ? "YES" : "NO");
        if (trap_registers.regs[index] != expected_arguments[index]) {
            goto cleanup;
        }
    }
    puts("HW_FUNCTION_ARGUMENT_CAPTURE=YES");

    if (restore_first_hw_break_slot(
            target_pid,
            &initial_hw_state.debug_registers[0],
            initial_hw_length) == -1) {
        print_errno("HW_BREAKPOINT_REMOVED");
        goto cleanup;
    }
    if (get_hw_break_state(target_pid, &readback_hw_state, &readback_hw_length) == -1) {
        print_errno("HW_BREAKPOINT_CLEAR_READBACK");
        goto cleanup;
    }
    if (readback_hw_length != initial_hw_length ||
            readback_hw_state.debug_registers[0].address !=
                initial_hw_state.debug_registers[0].address ||
            readback_hw_state.debug_registers[0].control !=
                initial_hw_state.debug_registers[0].control) {
        puts("HW_BREAKPOINT_REMOVED=NO");
        goto cleanup;
    }
    hw_state_dirty = 0;
    puts("HW_BREAKPOINT_REMOVED=YES");

    if (ptrace(PTRACE_DETACH, target_pid, NULL, NULL) == -1) {
        print_errno("HW_PTRACE_DETACH");
        goto cleanup;
    }
    attached = 0;
    target_stopped = 0;
    puts("HW_PTRACE_DETACH=SUCCESS");
    result = 0;

cleanup:
    if (attached && hw_state_dirty) {
        if (!target_stopped || restore_first_hw_break_slot(
                target_pid,
                &initial_hw_state.debug_registers[0],
                initial_hw_length) == -1) {
            puts("HW_CLEANUP_RESTORE=FAILURE");
            if (target_stopped) {
                terminate_stopped_tracee(target_pid);
            }
            return 1;
        }
        hw_state_dirty = 0;
        puts("HW_CLEANUP_RESTORE=SUCCESS");
    }

    if (attached) {
        if (!target_stopped || ptrace(PTRACE_DETACH, target_pid, NULL, NULL) == -1) {
            print_errno("HW_CLEANUP_DETACH");
            if (target_stopped) {
                terminate_stopped_tracee(target_pid);
            }
            return 1;
        }
        puts("HW_CLEANUP_DETACH=SUCCESS");
    }

    return result;
}
