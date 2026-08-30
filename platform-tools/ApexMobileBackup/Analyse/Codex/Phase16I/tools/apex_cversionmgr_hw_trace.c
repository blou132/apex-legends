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
#define DEFAULT_TRAP_TIMEOUT_MS 90000
#define MAX_TRAP_TIMEOUT_MS 120000
#define CVERSIONMGR_INIT_ELF_VA UINT64_C(0x00476180)
#define EXPECTED_ENTRY_WORD0 UINT64_C(0x52800004d10203ff)
#define EXPECTED_ENTRY_WORD1 UINT64_C(0x2a0403e552800023)

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

_Static_assert(sizeof(struct user_pt_regs) == 272U,
               "unexpected NT_PRSTATUS size");
_Static_assert(sizeof(struct hwdebug_register) == 16U,
               "unexpected hwdebug register size");
_Static_assert(sizeof(struct user_hwdebug_state_compat) == 264U,
               "unexpected hwdebug state size");
_Static_assert(offsetof(struct user_hwdebug_state_compat, debug_registers) == 8U,
               "unexpected hwdebug register offset");
_Static_assert(SLOT0_SET_IOV_LENGTH == 24U,
               "slot0 iovec must contain exactly one complete slot");

enum bounded_wait_result {
    BOUNDED_WAIT_ERROR = -1,
    BOUNDED_WAIT_EVENT = 0,
    BOUNDED_WAIT_TIMEOUT = 1,
};

static void print_errno(const char *operation) {
    printf("%s=FAILURE errno=%d message=%s\n",
           operation, errno, strerror(errno));
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

static int parse_timeout_ms(const char *text, int *result) {
    char *end = NULL;
    long seconds;

    errno = 0;
    seconds = strtol(text, &end, 10);
    if (errno != 0 || end == text || *end != '\0' || seconds <= 0 ||
            seconds > MAX_TRAP_TIMEOUT_MS / 1000) {
        return -1;
    }
    *result = (int) seconds * 1000;
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
        pid_t tid,
        int *status,
        int timeout_ms) {
    const struct timespec delay = {.tv_sec = 0, .tv_nsec = 10000000};
    struct timespec start;
    struct timespec now;

    if (clock_gettime(CLOCK_MONOTONIC, &start) != 0) {
        return BOUNDED_WAIT_ERROR;
    }
    for (;;) {
        pid_t wait_result = waitpid(tid, status, WNOHANG | __WALL);

        if (wait_result == tid) {
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
        pid_t tid,
        struct user_pt_regs *registers,
        size_t *length) {
    struct iovec register_set = {
        .iov_base = registers,
        .iov_len = sizeof(*registers),
    };

    memset(registers, 0, sizeof(*registers));
    if (ptrace(PTRACE_GETREGSET, tid, (void *) NT_PRSTATUS, &register_set) == -1) {
        return -1;
    }
    *length = register_set.iov_len;
    return 0;
}

static int get_hw_break_state(
        pid_t tid,
        struct user_hwdebug_state_compat *state,
        size_t *length) {
    struct iovec register_set = {
        .iov_base = state,
        .iov_len = sizeof(*state),
    };

    memset(state, 0, sizeof(*state));
    if (ptrace(PTRACE_GETREGSET, tid, (void *) NT_ARM_HW_BREAK, &register_set) == -1) {
        return -1;
    }
    *length = register_set.iov_len;
    return 0;
}

static int set_slot0_atomically(pid_t tid, uint64_t address, uint32_t control) {
    struct user_hwdebug_state_compat state;
    struct iovec register_set;

    memset(&state, 0, sizeof(state));
    state.debug_registers[0].address = address;
    state.debug_registers[0].control = control;
    register_set.iov_base = &state;
    register_set.iov_len = SLOT0_SET_IOV_LENGTH;

    return ptrace(PTRACE_SETREGSET, tid, (void *) NT_ARM_HW_BREAK, &register_set);
}

static int peek_u64(pid_t tid, uint64_t address, uint64_t *value) {
    long word;

    errno = 0;
    word = ptrace(PTRACE_PEEKDATA, tid, (void *) (uintptr_t) address, NULL);
    if (word == -1 && errno != 0) {
        return -1;
    }
    *value = (uint64_t) (unsigned long) word;
    return 0;
}

static int stop_running_tracee(pid_t tid, int *status) {
    enum bounded_wait_result wait_result;

    if (kill(tid, SIGSTOP) != 0) {
        return -1;
    }
    wait_result = bounded_waitpid(tid, status, ATTACH_TIMEOUT_MS);
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(*status)) {
        errno = ETIMEDOUT;
        return -1;
    }
    return 0;
}

int main(int argc, char **argv) {
    pid_t target_tid;
    uint64_t module_load_bias;
    uint64_t breakpoint_address;
    int trap_timeout_ms = DEFAULT_TRAP_TIMEOUT_MS;
    struct user_hwdebug_state_compat initial_hw_state;
    struct user_hwdebug_state_compat readback_hw_state;
    struct user_pt_regs registers;
    size_t initial_hw_length = 0;
    size_t readback_hw_length = 0;
    size_t register_length = 0;
    siginfo_t signal_info;
    enum bounded_wait_result wait_result;
    uint64_t callback_container = 0;
    uint64_t callback_object = 0;
    uint64_t callback_vptr = 0;
    uint64_t slot28_target = 0;
    uint64_t entry_word0 = 0;
    uint64_t entry_word1 = 0;
    unsigned int slot_count;
    unsigned int index;
    int status = 0;
    int attached = 0;
    int target_stopped = 0;
    int active_set_attempted = 0;
    int breakpoint_armed = 0;
    int disable_attempted = 0;
    int breakpoint_disabled = 0;
    int result = 1;

    if (argc != 3 && argc != 4) {
        fprintf(stderr,
                "usage: apex_cversionmgr_hw_trace <tid> <module_load_bias> "
                "[timeout_seconds]\n");
        return 2;
    }
    if (parse_positive_pid(argv[1], &target_tid) != 0 ||
            parse_uint64(argv[2], &module_load_bias) != 0 ||
            module_load_bias > UINT64_MAX - CVERSIONMGR_INIT_ELF_VA ||
            (argc == 4 && parse_timeout_ms(argv[3], &trap_timeout_ms) != 0)) {
        fprintf(stderr, "invalid argument\n");
        return 2;
    }
    breakpoint_address = module_load_bias + CVERSIONMGR_INIT_ELF_VA;
    if ((breakpoint_address & UINT64_C(3)) != 0U) {
        fprintf(stderr, "unaligned target address\n");
        return 2;
    }

    if (ptrace(PTRACE_ATTACH, target_tid, NULL, NULL) == -1) {
        print_errno("PTRACE_ATTACH_RESULT");
        return 1;
    }
    attached = 1;
    puts("PTRACE_ATTACH_RESULT=SUCCESS");

    wait_result = bounded_waitpid(target_tid, &status, ATTACH_TIMEOUT_MS);
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(status)) {
        puts("PTRACE_WAIT_STOP_RESULT=FAILURE");
        goto cleanup;
    }
    target_stopped = 1;
    printf("PTRACE_WAIT_STOP_RESULT=SUCCESS signal=%d\n", WSTOPSIG(status));

    if (get_registers(target_tid, &registers, &register_length) == -1) {
        print_errno("INITIAL_GETREGSET_RESULT");
        goto cleanup;
    }
    printf("REGISTER_SET_LENGTH=%zu\n", register_length);
    if (register_length != sizeof(registers)) {
        puts("REGISTER_SET_READY=NO");
        goto cleanup;
    }
    puts("REGISTER_SET_READY=YES");

    if (get_hw_break_state(target_tid, &initial_hw_state, &initial_hw_length) == -1) {
        print_errno("HW_BREAK_INITIAL_READ_RESULT");
        goto cleanup;
    }
    printf("HW_BREAK_REGSET_LENGTH=%zu\n", initial_hw_length);
    if (initial_hw_length != sizeof(initial_hw_state)) {
        puts("HW_BREAK_REGSET_LAYOUT_VALID=NO");
        goto cleanup;
    }
    puts("HW_BREAK_REGSET_LAYOUT_VALID=YES");

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

    if (peek_u64(target_tid, breakpoint_address, &entry_word0) != 0 ||
            peek_u64(target_tid, breakpoint_address + UINT64_C(8),
                     &entry_word1) != 0) {
        print_errno("CVERSIONMGR_RUNTIME_BYTES_READ_RESULT");
        goto cleanup;
    }
    printf("CVERSIONMGR_RUNTIME_ENTRY_WORD0=0x%016" PRIx64 "\n", entry_word0);
    printf("CVERSIONMGR_RUNTIME_ENTRY_WORD1=0x%016" PRIx64 "\n", entry_word1);
    printf("CVERSIONMGR_RUNTIME_BYTES_MATCH=%s\n",
           entry_word0 == EXPECTED_ENTRY_WORD0 &&
                   entry_word1 == EXPECTED_ENTRY_WORD1 ? "YES" : "NO");
    if (entry_word0 != EXPECTED_ENTRY_WORD0 ||
            entry_word1 != EXPECTED_ENTRY_WORD1) {
        goto cleanup;
    }

    puts("APEX_TRACE_ATTEMPT_COUNT=1");
    puts("REQUESTED_SLOT=0");
    printf("MODULE_LOAD_BIAS=0x%016" PRIx64 "\n", module_load_bias);
    printf("REQUESTED_ADDRESS=0x%016" PRIx64 "\n", breakpoint_address);
    printf("REQUESTED_CONTROL=0x%08" PRIx32 "\n", REQUESTED_HW_CTRL);
    active_set_attempted = 1;
    if (set_slot0_atomically(
            target_tid, breakpoint_address, REQUESTED_HW_CTRL) == -1) {
        print_errno("APEX_ACTIVE_SETREGSET_RESULT");
        goto cleanup;
    }
    breakpoint_armed = 1;
    puts("APEX_ACTIVE_SETREGSET_RESULT=SUCCESS");

    if (get_hw_break_state(
            target_tid, &readback_hw_state, &readback_hw_length) == -1) {
        print_errno("APEX_ACTIVE_READBACK_RESULT");
        goto cleanup;
    }
    printf("APEX_ACTIVE_RETURNED_ADDRESS=0x%016" PRIx64 "\n",
           readback_hw_state.debug_registers[0].address);
    printf("APEX_ACTIVE_CTRL_READBACK=0x%08" PRIx32 "\n",
           readback_hw_state.debug_registers[0].control);
    printf("APEX_ACTIVE_ADDRESS_MATCH=%s\n",
           readback_hw_state.debug_registers[0].address == breakpoint_address ?
               "YES" : "NO");
    printf("APEX_ACTIVE_CTRL_MATCH=%s\n",
           readback_hw_state.debug_registers[0].control ==
                   EXPECTED_CACHED_HW_CTRL ? "YES" : "NO");
    if (readback_hw_length != initial_hw_length ||
            readback_hw_state.debug_registers[0].address != breakpoint_address ||
            readback_hw_state.debug_registers[0].control !=
                    EXPECTED_CACHED_HW_CTRL) {
        goto cleanup;
    }

    if (ptrace(PTRACE_CONT, target_tid, NULL, NULL) == -1) {
        print_errno("PTRACE_CONT_RESULT");
        goto cleanup;
    }
    target_stopped = 0;
    wait_result = bounded_waitpid(target_tid, &status, trap_timeout_ms);
    if (wait_result == BOUNDED_WAIT_TIMEOUT) {
        puts("APEX_HW_BREAKPOINT_WAIT_RESULT=TIMEOUT");
        if (stop_running_tracee(target_tid, &status) == 0) {
            target_stopped = 1;
        }
        goto cleanup;
    }
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(status)) {
        puts("APEX_HW_BREAKPOINT_WAIT_RESULT=FAILURE");
        if (WIFEXITED(status) || WIFSIGNALED(status)) {
            attached = 0;
        }
        goto cleanup;
    }
    target_stopped = 1;
    printf("APEX_HW_BREAKPOINT_STOP_SIGNAL=%d\n", WSTOPSIG(status));
    if (WSTOPSIG(status) != SIGTRAP) {
        puts("APEX_SIGTRAP_RECEIVED=NO");
        goto cleanup;
    }
    puts("APEX_SIGTRAP_RECEIVED=YES");

    memset(&signal_info, 0, sizeof(signal_info));
    if (ptrace(PTRACE_GETSIGINFO, target_tid, NULL, &signal_info) == -1) {
        print_errno("APEX_SIGTRAP_INFO_RESULT");
        goto cleanup;
    }
    printf("APEX_SIGTRAP_CODE=%d\n", signal_info.si_code);
    printf("APEX_SIGTRAP_HW_CLASSIFICATION=%s\n",
           signal_info.si_code == TRAP_HWBKPT ? "YES" : "NO");
    printf("APEX_SIGTRAP_ADDRESS_MATCH=%s\n",
           (uintptr_t) signal_info.si_addr == breakpoint_address ? "YES" : "NO");
    if (signal_info.si_code != TRAP_HWBKPT ||
            (uintptr_t) signal_info.si_addr != breakpoint_address) {
        goto cleanup;
    }

    if (get_registers(target_tid, &registers, &register_length) == -1 ||
            register_length != sizeof(registers)) {
        print_errno("TRAP_GETREGSET_RESULT");
        goto cleanup;
    }
    printf("PC=0x%016" PRIx64 "\n", (uint64_t) registers.pc);
    printf("SP=0x%016" PRIx64 "\n", (uint64_t) registers.sp);
    printf("LR=0x%016" PRIx64 "\n", (uint64_t) registers.regs[30]);
    printf("PSTATE=0x%016" PRIx64 "\n", (uint64_t) registers.pstate);
    for (index = 0; index < 8U; index++) {
        printf("X%u=0x%016" PRIx64 "\n",
               index, (uint64_t) registers.regs[index]);
    }
    printf("CVERSIONMGR_INIT_ENTRY_TRAP=%s\n",
           registers.pc == breakpoint_address ? "YES" : "NO");
    if (registers.pc != breakpoint_address) {
        goto cleanup;
    }

    callback_container = registers.regs[1];
    printf("EXTERNAL_CALLBACK_CONTAINER=0x%016" PRIx64 "\n",
           callback_container);
    if (callback_container == 0U) {
        puts("EXTERNAL_CALLBACK_POINTER_READ=FAILURE null_container");
        goto cleanup;
    }
    if (peek_u64(target_tid, callback_container, &callback_object) != 0) {
        print_errno("EXTERNAL_CALLBACK_POINTER_READ");
        goto cleanup;
    }
    printf("EXTERNAL_CALLBACK_OBJECT=0x%016" PRIx64 "\n", callback_object);
    if (callback_object == 0U) {
        puts("EXTERNAL_CALLBACK_VPTR_READ=FAILURE null_callback");
        goto cleanup;
    }
    if (peek_u64(target_tid, callback_object, &callback_vptr) != 0) {
        print_errno("EXTERNAL_CALLBACK_VPTR_READ");
        goto cleanup;
    }
    printf("EXTERNAL_CALLBACK_VPTR=0x%016" PRIx64 "\n", callback_vptr);
    if (callback_vptr == 0U || callback_vptr > UINT64_MAX - UINT64_C(0x28)) {
        puts("EXTERNAL_CALLBACK_SLOT28_READ=FAILURE invalid_vptr");
        goto cleanup;
    }
    if (peek_u64(target_tid, callback_vptr + UINT64_C(0x28),
                 &slot28_target) != 0) {
        print_errno("EXTERNAL_CALLBACK_SLOT28_READ");
        goto cleanup;
    }
    printf("EXTERNAL_CALLBACK_SLOT28_TARGET=0x%016" PRIx64 "\n",
           slot28_target);
    puts("BOUNDED_CALLBACK_READS_COMPLETE=YES");

    disable_attempted = 1;
    if (set_slot0_atomically(target_tid, breakpoint_address, 0U) == -1) {
        print_errno("APEX_HW_BREAK_DISABLE_RESULT");
        goto cleanup;
    }
    breakpoint_armed = 0;
    breakpoint_disabled = 1;
    puts("APEX_HW_BREAK_DISABLE_RESULT=SUCCESS");

    if (ptrace(PTRACE_DETACH, target_tid, NULL, NULL) == -1) {
        print_errno("APEX_PTRACE_DETACH_RESULT");
        goto cleanup;
    }
    attached = 0;
    target_stopped = 0;
    puts("APEX_PTRACE_DETACH_RESULT=SUCCESS");
    result = 0;

cleanup:
    if (result != 0 && attached) {
        if (!target_stopped && stop_running_tracee(target_tid, &status) == 0) {
            target_stopped = 1;
        }
        if (target_stopped && breakpoint_armed && !disable_attempted) {
            disable_attempted = 1;
            if (set_slot0_atomically(target_tid, breakpoint_address, 0U) == 0) {
                breakpoint_armed = 0;
                breakpoint_disabled = 1;
                puts("APEX_FAILURE_CLEANUP_DISABLE=SUCCESS");
            } else {
                print_errno("APEX_FAILURE_CLEANUP_DISABLE");
            }
        }
        if (target_stopped && breakpoint_disabled) {
            if (ptrace(PTRACE_DETACH, target_tid, NULL, NULL) == 0) {
                attached = 0;
                puts("APEX_FAILURE_CLEANUP_DETACH=SUCCESS");
            }
        }
        if (target_stopped && !active_set_attempted && !breakpoint_armed &&
                !breakpoint_disabled) {
            if (ptrace(PTRACE_DETACH, target_tid, NULL, NULL) == 0) {
                attached = 0;
                puts("APEX_PREACTIVE_CLEANUP_DETACH=SUCCESS");
            }
        }
        if (attached) {
            (void) ptrace(PTRACE_KILL, target_tid, NULL, NULL);
            (void) waitpid(target_tid, &status, __WALL);
            puts("APEX_TRACED_TID_TERMINATED_FOR_SAFETY=YES");
        }
    }

    printf("BREAKPOINT_DISABLED_BEFORE_EXIT=%s\n",
           breakpoint_disabled ? "YES" : "NO_NOT_ARMED_OR_DISABLE_FAILED");
    fflush(stdout);
    return result;
}
