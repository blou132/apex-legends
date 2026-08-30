#include <asm/ptrace.h>
#include <errno.h>
#include <fcntl.h>
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
#include <time.h>
#include <unistd.h>

#ifndef NT_ARM_HW_BREAK
#define NT_ARM_HW_BREAK 0x402
#endif

#ifndef TRAP_HWBKPT
#define TRAP_HWBKPT 4
#endif

#define ARM_MAX_BRP 16U
#define EXPECTED_SLOT_COUNT 6U
#define REQUESTED_HW_CTRL UINT32_C(0x000001e5)
#define EXPECTED_CACHED_HW_CTRL UINT32_C(0x000041e4)
#define ATTACH_TIMEOUT_MS 5000
#define TRAP_TIMEOUT_MS 15000
#define HANDSHAKE_TIMEOUT_MS 5000
#define START_GATE_PATH "/data/local/tmp/phase16h_w_start_gate"

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

#define SLOT0_SET_IOV_LENGTH \
    offsetof(struct user_hwdebug_state_compat, debug_registers[1])
#define SLOT0_ADDRESS_OFFSET \
    (offsetof(struct user_hwdebug_state_compat, debug_registers) + \
     offsetof(struct hwdebug_register, address))
#define SLOT0_CONTROL_OFFSET \
    (offsetof(struct user_hwdebug_state_compat, debug_registers) + \
     offsetof(struct hwdebug_register, control))
#define SLOT0_PAD_OFFSET \
    (offsetof(struct user_hwdebug_state_compat, debug_registers) + \
     offsetof(struct hwdebug_register, pad))

_Static_assert(sizeof(struct hwdebug_register) == 16U,
               "unexpected hwdebug register size");
_Static_assert(sizeof(struct user_hwdebug_state_compat) == 264U,
               "unexpected hwdebug state size");
_Static_assert(offsetof(struct user_hwdebug_state_compat, debug_info) == 0U,
               "unexpected debug_info offset");
_Static_assert(offsetof(struct user_hwdebug_state_compat, pad) == 4U,
               "unexpected header pad offset");
_Static_assert(offsetof(struct user_hwdebug_state_compat, debug_registers) == 8U,
               "unexpected hwdebug register offset");
_Static_assert(offsetof(struct hwdebug_register, address) == 0U,
               "unexpected slot address offset");
_Static_assert(offsetof(struct hwdebug_register, control) == 8U,
               "unexpected slot control offset");
_Static_assert(offsetof(struct hwdebug_register, pad) == 12U,
               "unexpected slot pad offset");
_Static_assert(SLOT0_ADDRESS_OFFSET == 8U,
               "unexpected slot0 address offset");
_Static_assert(SLOT0_CONTROL_OFFSET == 16U,
               "unexpected slot0 control offset");
_Static_assert(SLOT0_PAD_OFFSET == 20U,
               "unexpected slot0 pad offset");
_Static_assert(SLOT0_SET_IOV_LENGTH == 24U,
               "slot0 iovec must contain header and exactly one complete slot");

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

enum bounded_wait_result {
    BOUNDED_WAIT_ERROR = -1,
    BOUNDED_WAIT_EVENT = 0,
    BOUNDED_WAIT_TIMEOUT = 1,
};

static void print_errno(const char *operation) {
    printf("%s=FAILURE errno=%d message=%s\n", operation, errno, strerror(errno));
    fflush(stdout);
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

static int64_t elapsed_milliseconds(
        const struct timespec *start,
        const struct timespec *now) {
    int64_t seconds = (int64_t) now->tv_sec - (int64_t) start->tv_sec;
    int64_t nanoseconds = (int64_t) now->tv_nsec - (int64_t) start->tv_nsec;

    return seconds * INT64_C(1000) + nanoseconds / INT64_C(1000000);
}

static enum bounded_wait_result bounded_waitpid(
        pid_t pid,
        int *status,
        int timeout_ms) {
    const struct timespec delay = {.tv_sec = 0, .tv_nsec = 10000000};
    struct timespec start;
    struct timespec now;

    if (clock_gettime(CLOCK_MONOTONIC, &start) != 0) {
        return BOUNDED_WAIT_ERROR;
    }

    for (;;) {
        pid_t wait_result = waitpid(pid, status, WNOHANG | __WALL);

        if (wait_result == pid) {
            return BOUNDED_WAIT_EVENT;
        }
        if (wait_result == -1) {
            return BOUNDED_WAIT_ERROR;
        }
        if (clock_gettime(CLOCK_MONOTONIC, &now) != 0) {
            return BOUNDED_WAIT_ERROR;
        }
        if (elapsed_milliseconds(&start, &now) >= timeout_ms) {
            return BOUNDED_WAIT_TIMEOUT;
        }
        nanosleep(&delay, NULL);
    }
}

static int get_registers(
        pid_t pid,
        struct user_pt_regs *registers,
        size_t *length) {
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

static int set_slot0_atomically(pid_t pid, uint64_t address, uint32_t control) {
    struct user_hwdebug_state_compat state;
    struct iovec register_set;

    memset(&state, 0, sizeof(state));
    state.debug_registers[0].address = address;
    state.debug_registers[0].control = control;

    register_set.iov_base = &state;
    register_set.iov_len = SLOT0_SET_IOV_LENGTH;

    return ptrace(PTRACE_SETREGSET, pid, (void *) NT_ARM_HW_BREAK, &register_set);
}

static int explicitly_disable_breakpoint(
        pid_t pid,
        uint64_t address,
        size_t expected_length,
        struct hwdebug_register *cached_slot,
        size_t *readback_length,
        int *readback_available) {
    struct user_hwdebug_state_compat state;
    size_t length = 0;

    memset(cached_slot, 0, sizeof(*cached_slot));
    *readback_length = 0;
    *readback_available = 0;

    if (set_slot0_atomically(pid, address, 0U) == -1) {
        return -1;
    }

    if (get_hw_break_state(pid, &state, &length) == 0 &&
            length == expected_length) {
        *cached_slot = state.debug_registers[0];
        *readback_length = length;
        *readback_available = 1;
    }
    return 0;
}

static int stop_running_tracee(pid_t pid, int *status) {
    enum bounded_wait_result wait_result;

    if (kill(pid, SIGSTOP) != 0) {
        return -1;
    }
    wait_result = bounded_waitpid(pid, status, ATTACH_TIMEOUT_MS);
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(*status)) {
        errno = ETIMEDOUT;
        return -1;
    }
    return 0;
}

static int create_start_gate(void) {
    int descriptor = open(START_GATE_PATH, O_CREAT | O_EXCL | O_WRONLY, 0600);

    if (descriptor == -1) {
        return -1;
    }
    if (close(descriptor) != 0) {
        (void) unlink(START_GATE_PATH);
        return -1;
    }
    return 0;
}

static void terminate_tracee(pid_t pid, int attached, int stopped) {
    int status = 0;

    if (attached && stopped) {
        if (ptrace(PTRACE_KILL, pid, NULL, NULL) == 0) {
            (void) waitpid(pid, &status, __WALL);
            puts("TRACEE_TERMINATED_FOR_SAFETY=YES");
            return;
        }
    }

    (void) kill(pid, SIGKILL);
    if (attached) {
        (void) waitpid(pid, &status, __WALL);
    }
    puts("TRACEE_TERMINATED_FOR_SAFETY=YES");
}

int main(int argc, char **argv) {
    pid_t target_pid;
    uint64_t breakpoint_address;
    struct user_hwdebug_state_compat initial_hw_state;
    struct user_hwdebug_state_compat readback_hw_state;
    struct hwdebug_register disable_readback_slot;
    struct user_pt_regs registers;
    size_t initial_hw_length = 0;
    size_t readback_hw_length = 0;
    size_t register_length = 0;
    siginfo_t signal_info;
    enum bounded_wait_result wait_result;
    size_t disable_readback_length = 0;
    unsigned int slot_count;
    unsigned int index;
    int status = 0;
    int attached = 0;
    int target_stopped = 0;
    int breakpoint_armed = 0;
    int breakpoint_disabled = 0;
    int disable_attempted = 0;
    int disable_readback_available = 0;
    int start_gate_created = 0;
    int result = 1;

    if (argc != 3) {
        fprintf(stderr, "usage: ptrace_hw_breakpoint_atomic <pid> <address>\n");
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

    wait_result = bounded_waitpid(target_pid, &status, ATTACH_TIMEOUT_MS);
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(status)) {
        puts("PTRACE_WAIT_STOP_RESULT=FAILURE");
        goto cleanup;
    }
    target_stopped = 1;
    printf("PTRACE_WAIT_STOP_RESULT=SUCCESS signal=%d\n", WSTOPSIG(status));

    if (get_registers(target_pid, &registers, &register_length) == -1) {
        print_errno("INITIAL_GETREGSET_RESULT");
        goto cleanup;
    }
    printf("REGISTER_SET_LENGTH=%zu\n", register_length);
    if (register_length != sizeof(registers)) {
        puts("REGISTER_SET_READY=NO");
        goto cleanup;
    }
    puts("REGISTER_SET_READY=YES");

    if (get_hw_break_state(target_pid, &initial_hw_state, &initial_hw_length) == -1) {
        print_errno("HW_BREAK_INITIAL_READ_OK");
        goto cleanup;
    }
    puts("HW_BREAK_INITIAL_READ_OK=YES");
    printf("HW_BREAK_REGSET_LENGTH=%zu\n", initial_hw_length);
    if (initial_hw_length < offsetof(struct user_hwdebug_state_compat, debug_registers) ||
            initial_hw_length > sizeof(initial_hw_state)) {
        puts("HW_BREAK_REGSET_LAYOUT_VALID=NO");
        goto cleanup;
    }

    slot_count = initial_hw_state.debug_info & UINT32_C(0xff);
    printf("HW_EXEC_BREAKPOINT_SLOTS=%u\n", slot_count);
    if (slot_count != EXPECTED_SLOT_COUNT) {
        puts("HW_BREAKPOINT_RESOURCE_VALID=NO");
        goto cleanup;
    }
    for (index = 0; index < slot_count; index++) {
        if (initial_hw_state.debug_registers[index].control != 0U) {
            puts("HW_BREAKPOINT_INITIAL_STATE_CLEAR=NO");
            goto cleanup;
        }
    }
    puts("HW_BREAKPOINT_INITIAL_STATE_CLEAR=YES");

    puts("REQUESTED_SLOT=0");
    printf("REQUESTED_ADDRESS=0x%016" PRIx64 "\n", breakpoint_address);
    printf("REQUESTED_CONTROL=0x%08" PRIx32 "\n", REQUESTED_HW_CTRL);
    printf("SETREGSET_IOV_LENGTH=%zu\n", (size_t) SLOT0_SET_IOV_LENGTH);
    if (set_slot0_atomically(
            target_pid, breakpoint_address, REQUESTED_HW_CTRL) == -1) {
        print_errno("HW_BREAK_CTRL_SET_RESULT");
        goto cleanup;
    }
    breakpoint_armed = 1;
    puts("HW_BREAK_CTRL_SET_RESULT=SUCCESS");

    if (get_hw_break_state(target_pid, &readback_hw_state, &readback_hw_length) == -1) {
        print_errno("HW_BREAK_CTRL_READBACK_RESULT");
        goto cleanup;
    }
    printf("RETURNED_ADDRESS=0x%016" PRIx64 "\n",
           readback_hw_state.debug_registers[0].address);
    printf("RETURNED_CONTROL=0x%08" PRIx32 "\n",
           readback_hw_state.debug_registers[0].control);
    printf("RETURNED_REGSET_LENGTH=%zu\n", readback_hw_length);
    printf("ADDRESS_READBACK_MATCH=%s\n",
           readback_hw_state.debug_registers[0].address == breakpoint_address ?
               "YES" : "NO");
    if (readback_hw_length != initial_hw_length ||
            readback_hw_state.debug_registers[0].address != breakpoint_address ||
            readback_hw_state.debug_registers[0].control != EXPECTED_CACHED_HW_CTRL) {
        puts("HW_BREAK_NORMALIZED_READBACK_EXPECTED=NO");
        goto cleanup;
    }
    puts("HW_BREAK_NORMALIZED_READBACK_EXPECTED=YES");
    puts("HW_BREAKPOINT_ARMED_FOR_TEST=YES");

    if (create_start_gate() != 0) {
        print_errno("START_GATE_CREATE_RESULT");
        goto cleanup;
    }
    start_gate_created = 1;
    puts("START_GATE_CREATE_RESULT=SUCCESS");

    if (ptrace(PTRACE_CONT, target_pid, NULL, NULL) == -1) {
        print_errno("PTRACE_CONT_RESULT");
        goto cleanup;
    }
    target_stopped = 0;
    wait_result = bounded_waitpid(target_pid, &status, TRAP_TIMEOUT_MS);
    if (wait_result == BOUNDED_WAIT_TIMEOUT) {
        puts("HW_BREAKPOINT_WAIT_RESULT=TIMEOUT");
        if (stop_running_tracee(target_pid, &status) == 0) {
            target_stopped = 1;
        }
        goto cleanup;
    }
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(status)) {
        puts("HW_BREAKPOINT_WAIT_RESULT=FAILURE");
        if (WIFEXITED(status) || WIFSIGNALED(status)) {
            attached = 0;
        }
        goto cleanup;
    }
    target_stopped = 1;
    printf("HW_BREAKPOINT_STOP_SIGNAL=%d\n", WSTOPSIG(status));
    if (WSTOPSIG(status) != SIGTRAP) {
        puts("SIGTRAP_RECEIVED=NO");
        goto cleanup;
    }
    puts("SIGTRAP_RECEIVED=YES");

    memset(&signal_info, 0, sizeof(signal_info));
    if (ptrace(PTRACE_GETSIGINFO, target_pid, NULL, &signal_info) == -1) {
        print_errno("HW_SIGTRAP_INFO_CAPTURED");
        goto cleanup;
    }
    printf("SIGTRAP_CODE=%d\n", signal_info.si_code);
    printf("SIGTRAP_CODE_IS_TRAP_HWBKPT=%s\n",
           signal_info.si_code == TRAP_HWBKPT ? "YES" : "NO");
    printf("SIGTRAP_ADDRESS_MATCH=%s\n",
           (uintptr_t) signal_info.si_addr == breakpoint_address ? "YES" : "NO");
    if ((uintptr_t) signal_info.si_addr != breakpoint_address) {
        goto cleanup;
    }

    if (get_registers(target_pid, &registers, &register_length) == -1 ||
            register_length != sizeof(registers)) {
        print_errno("TRAP_GETREGSET_RESULT");
        goto cleanup;
    }
    printf("PC=0x%016" PRIx64 "\n", (uint64_t) registers.pc);
    printf("SP=0x%016" PRIx64 "\n", (uint64_t) registers.sp);
    printf("LR=0x%016" PRIx64 "\n", (uint64_t) registers.regs[30]);
    printf("FP=0x%016" PRIx64 "\n", (uint64_t) registers.regs[29]);
    printf("PSTATE=0x%016" PRIx64 "\n", (uint64_t) registers.pstate);
    if (registers.pc != breakpoint_address) {
        puts("EXACT_FUNCTION_ENTRY_TRAP=NO");
        goto cleanup;
    }
    puts("EXACT_FUNCTION_ENTRY_TRAP=YES");
    puts("PC_CAPTURED=YES");
    puts("SP_CAPTURED=YES");
    puts("LR_CAPTURED=YES");

    for (index = 0; index < 8U; index++) {
        printf("X%u=0x%016" PRIx64 "\n", index,
               (uint64_t) registers.regs[index]);
        printf("X%u_MATCH=%s\n", index,
               registers.regs[index] == expected_arguments[index] ? "YES" : "NO");
        if (registers.regs[index] != expected_arguments[index]) {
            goto cleanup;
        }
    }
    puts("FUNCTION_ARGUMENT_CAPTURE=YES");

    disable_attempted = 1;
    if (explicitly_disable_breakpoint(
            target_pid,
            breakpoint_address,
            initial_hw_length,
            &disable_readback_slot,
            &disable_readback_length,
            &disable_readback_available) == -1) {
        print_errno("HW_BREAK_DISABLE_SET_RESULT");
        goto cleanup;
    }
    breakpoint_armed = 0;
    breakpoint_disabled = 1;
    puts("HW_BREAK_DISABLE_SET_RESULT=SUCCESS");
    puts("DISABLE_ADDRESS_READBACK_REQUIRED=NO");
    printf("HW_BREAK_DISABLE_READBACK_AVAILABLE=%s\n",
           disable_readback_available ? "YES" : "NO");
    if (disable_readback_available) {
        printf("HW_BREAK_DISABLE_RETURNED_ADDRESS=0x%016" PRIx64 "\n",
               disable_readback_slot.address);
        printf("HW_BREAK_DISABLE_RETURNED_CONTROL=0x%08" PRIx32 "\n",
               disable_readback_slot.control);
        printf("HW_BREAK_DISABLE_RETURNED_LENGTH=%zu\n",
               disable_readback_length);
    }

    if (ptrace(PTRACE_CONT, target_pid, NULL, NULL) == -1) {
        print_errno("POST_DISABLE_CONT_RESULT");
        goto cleanup;
    }
    target_stopped = 0;
    wait_result = bounded_waitpid(target_pid, &status, HANDSHAKE_TIMEOUT_MS);
    if (wait_result == BOUNDED_WAIT_TIMEOUT) {
        puts("POST_DISABLE_HANDSHAKE=TIMEOUT");
        if (stop_running_tracee(target_pid, &status) == 0) {
            target_stopped = 1;
        }
        goto cleanup;
    }
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(status)) {
        puts("POST_DISABLE_HANDSHAKE=FAILURE");
        if (WIFEXITED(status) || WIFSIGNALED(status)) {
            attached = 0;
        }
        goto cleanup;
    }
    target_stopped = 1;
    printf("POST_DISABLE_STOP_SIGNAL=%d\n", WSTOPSIG(status));
    if (WSTOPSIG(status) == SIGTRAP) {
        puts("POST_DISABLE_RETRAP=YES");
        goto cleanup;
    }
    if (WSTOPSIG(status) != SIGSTOP) {
        puts("POST_DISABLE_FUNCTION_EXECUTION_OBSERVED=NO");
        goto cleanup;
    }
    puts("POST_DISABLE_FUNCTION_EXECUTION_OBSERVED=YES");
    puts("POST_DISABLE_RETRAP=NO");
    puts("HW_BREAK_DISABLED_BY_PROVEN_PATH=YES");

    if (ptrace(PTRACE_DETACH, target_pid, NULL, NULL) == -1) {
        print_errno("PTRACE_DETACH_RESULT");
        goto cleanup;
    }
    attached = 0;
    target_stopped = 0;
    puts("PTRACE_DETACH_RESULT=SUCCESS");
    result = 0;

cleanup:
    if (start_gate_created) {
        (void) unlink(START_GATE_PATH);
    }
    if (result != 0 && attached) {
        if (!target_stopped && stop_running_tracee(target_pid, &status) == 0) {
            target_stopped = 1;
        }
        if (target_stopped && breakpoint_armed && !disable_attempted) {
            disable_attempted = 1;
            if (explicitly_disable_breakpoint(
                    target_pid,
                    breakpoint_address,
                    initial_hw_length,
                    &disable_readback_slot,
                    &disable_readback_length,
                    &disable_readback_available) == 0) {
                breakpoint_armed = 0;
                breakpoint_disabled = 1;
                printf("HW_FAILURE_CLEANUP_DISABLE=SUCCESS readback=%s\n",
                       disable_readback_available ? "AVAILABLE" : "UNAVAILABLE");
            } else {
                print_errno("HW_FAILURE_CLEANUP_DISABLE");
            }
        }
        terminate_tracee(target_pid, attached, target_stopped);
    }

    printf("BREAKPOINT_DISABLED_BEFORE_EXIT=%s\n",
           breakpoint_disabled ? "YES" : "NO_NOT_ARMED_OR_DISABLE_FAILED");
    fflush(stdout);
    return result;
}
