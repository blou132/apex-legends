/* Bounded read-only observation with hardware execution breakpoints. */
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
#define STEP_TIMEOUT_MS 5000
#define RETURN_TIMEOUT_MS 30000
#define MAX_ENTRY_TIMEOUT_SECONDS 180
#define MAX_IMMEDIATE_STEPS 3U
#define EXPECTED_CALLSITE_WORD0 UINT64_C(0xaa0003e195264109)
#define EXPECTED_CALLSITE_WORD1 UINT64_C(0x910103e2d100e3a0)
#define EXPECTED_PLT_WORD0 UINT64_C(0xf9446611b0007df0)
#define EXPECTED_PLT_WORD1 UINT64_C(0xd61f022091232210)
#define EXPECTED_PROVIDER_WORD0 UINT64_C(0xd2800600d10043ff)
#define EXPECTED_PROVIDER_WORD1 UINT64_C(0x94058d7ba9007bf3)

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
               "unexpected hardware debug register size");
_Static_assert(sizeof(struct user_hwdebug_state_compat) == 264U,
               "unexpected hardware debug state size");
_Static_assert(SLOT0_SET_IOV_LENGTH == 24U,
               "slot0 iovec must contain one complete slot");

enum bounded_wait_result {
    BOUNDED_WAIT_ERROR = -1,
    BOUNDED_WAIT_EVENT = 0,
    BOUNDED_WAIT_TIMEOUT = 1,
};

struct trace_context {
    pid_t tid;
    int attached;
    int stopped;
    int active_programming_started;
    int breakpoint_armed;
    uint64_t breakpoint_address;
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

static int parse_u64(const char *text, uint64_t *result) {
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

static int parse_timeout(const char *text, int *timeout_ms) {
    char *end = NULL;
    long seconds;

    errno = 0;
    seconds = strtol(text, &end, 10);
    if (errno != 0 || end == text || *end != '\0' || seconds <= 0 ||
            seconds > MAX_ENTRY_TIMEOUT_SECONDS) {
        return -1;
    }
    *timeout_ms = (int) seconds * 1000;
    return 0;
}

static int64_t elapsed_ms(
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
        if (elapsed_ms(&start, &now) >= timeout_ms) {
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

static int get_hw_state(
        pid_t tid,
        struct user_hwdebug_state_compat *state,
        size_t *length) {
    struct iovec register_set = {
        .iov_base = state,
        .iov_len = sizeof(*state),
    };

    memset(state, 0, sizeof(*state));
    if (ptrace(PTRACE_GETREGSET, tid, (void *) NT_ARM_HW_BREAK,
               &register_set) == -1) {
        return -1;
    }
    *length = register_set.iov_len;
    return 0;
}

static int set_slot0(pid_t tid, uint64_t address, uint32_t control) {
    struct user_hwdebug_state_compat state;
    struct iovec register_set;

    memset(&state, 0, sizeof(state));
    state.debug_registers[0].address = address;
    state.debug_registers[0].control = control;
    register_set.iov_base = &state;
    register_set.iov_len = SLOT0_SET_IOV_LENGTH;

    return ptrace(PTRACE_SETREGSET, tid, (void *) NT_ARM_HW_BREAK,
                  &register_set);
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

static int stop_running_tracee(struct trace_context *context, int *status) {
    enum bounded_wait_result wait_result;

    if (kill(context->tid, SIGSTOP) != 0) {
        return -1;
    }
    wait_result = bounded_waitpid(context->tid, status, ATTACH_TIMEOUT_MS);
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(*status)) {
        errno = ETIMEDOUT;
        return -1;
    }
    context->stopped = 1;
    return 0;
}

static int attach_and_validate(struct trace_context *context) {
    struct user_pt_regs registers;
    struct user_hwdebug_state_compat hw_state;
    size_t register_length = 0;
    size_t hw_length = 0;
    unsigned int slot_count;
    unsigned int index;
    enum bounded_wait_result wait_result;
    int status = 0;

    if (ptrace(PTRACE_ATTACH, context->tid, NULL, NULL) == -1) {
        print_errno("PTRACE_ATTACH_RESULT");
        return -1;
    }
    context->attached = 1;
    puts("PTRACE_ATTACH_RESULT=SUCCESS");

    wait_result = bounded_waitpid(context->tid, &status, ATTACH_TIMEOUT_MS);
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(status)) {
        puts("PTRACE_WAIT_STOP_RESULT=FAILURE");
        return -1;
    }
    context->stopped = 1;
    printf("PTRACE_WAIT_STOP_RESULT=SUCCESS signal=%d\n", WSTOPSIG(status));

    if (get_registers(context->tid, &registers, &register_length) != 0) {
        print_errno("GENERAL_GETREGSET_RESULT");
        return -1;
    }
    printf("GENERAL_REGSET_LENGTH=%zu\n", register_length);
    if (register_length != sizeof(registers)) {
        puts("GENERAL_REGSET_VALID=NO");
        return -1;
    }
    puts("GENERAL_REGSET_VALID=YES");

    if (get_hw_state(context->tid, &hw_state, &hw_length) != 0) {
        print_errno("HW_GETREGSET_RESULT");
        return -1;
    }
    printf("HW_REGSET_LENGTH=%zu\n", hw_length);
    if (hw_length != sizeof(hw_state)) {
        puts("HW_REGSET_VALID=NO");
        return -1;
    }
    puts("HW_REGSET_VALID=YES");

    slot_count = hw_state.debug_info & UINT32_C(0xff);
    printf("HW_EXEC_SLOTS=%u\n", slot_count);
    if (slot_count != EXPECTED_SLOT_COUNT) {
        puts("HW_SLOT_COUNT_VALID=NO");
        return -1;
    }
    for (index = 0; index < slot_count; index++) {
        if (hw_state.debug_registers[index].control != 0U) {
            puts("HW_INITIAL_STATE_CLEAR=NO");
            return -1;
        }
    }
    puts("HW_INITIAL_STATE_CLEAR=YES");
    return 0;
}

static int disable_breakpoint(struct trace_context *context, const char *label) {
    if (!context->breakpoint_armed) {
        printf("%s=NOT_ARMED\n", label);
        return 0;
    }
    if (set_slot0(context->tid, context->breakpoint_address, 0U) != 0) {
        print_errno(label);
        return -1;
    }
    context->breakpoint_armed = 0;
    printf("%s=SUCCESS\n", label);
    return 0;
}

static int program_breakpoint(
        struct trace_context *context,
        uint64_t address,
        const char *set_label,
        const char *address_label,
        const char *control_label) {
    struct user_hwdebug_state_compat readback;
    size_t readback_length = 0;

    context->active_programming_started = 1;
    context->breakpoint_address = address;
    if (set_slot0(context->tid, address, REQUESTED_HW_CTRL) != 0) {
        print_errno(set_label);
        return -1;
    }
    context->breakpoint_armed = 1;
    printf("%s=SUCCESS\n", set_label);

    if (get_hw_state(context->tid, &readback, &readback_length) != 0) {
        print_errno("ACTIVE_READBACK_RESULT");
        return -1;
    }
    printf("%s=%s\n", address_label,
           readback.debug_registers[0].address == address ? "YES" : "NO");
    printf("ACTIVE_CTRL_READBACK=0x%08" PRIx32 "\n",
           readback.debug_registers[0].control);
    printf("%s=%s\n", control_label,
           readback_length == sizeof(readback) &&
                   readback.debug_registers[0].control ==
                           EXPECTED_CACHED_HW_CTRL ? "YES" : "NO");
    if (readback_length != sizeof(readback) ||
            readback.debug_registers[0].address != address ||
            readback.debug_registers[0].control != EXPECTED_CACHED_HW_CTRL) {
        return -1;
    }
    return 0;
}

static int continue_to_hw_trap(
        struct trace_context *context,
        uint64_t expected_pc,
        int timeout_ms,
        const char *wait_label,
        const char *trap_label,
        int *status) {
    enum bounded_wait_result wait_result;
    siginfo_t signal_info;
    struct user_pt_regs registers;
    size_t register_length = 0;

    if (ptrace(PTRACE_CONT, context->tid, NULL, NULL) == -1) {
        print_errno("PTRACE_CONT_RESULT");
        return -1;
    }
    context->stopped = 0;
    wait_result = bounded_waitpid(context->tid, status, timeout_ms);
    if (wait_result == BOUNDED_WAIT_TIMEOUT) {
        printf("%s=TIMEOUT\n", wait_label);
        if (stop_running_tracee(context, status) != 0) {
            print_errno("TIMEOUT_STOP_RESULT");
        }
        return 1;
    }
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(*status)) {
        printf("%s=FAILURE\n", wait_label);
        if (WIFEXITED(*status) || WIFSIGNALED(*status)) {
            context->attached = 0;
        }
        return -1;
    }
    context->stopped = 1;
    printf("%s=EVENT signal=%d\n", wait_label, WSTOPSIG(*status));
    if (WSTOPSIG(*status) != SIGTRAP) {
        printf("%s=NO\n", trap_label);
        return -1;
    }

    memset(&signal_info, 0, sizeof(signal_info));
    if (ptrace(PTRACE_GETSIGINFO, context->tid, NULL, &signal_info) == -1) {
        print_errno("GETSIGINFO_RESULT");
        return -1;
    }
    printf("%s_SIGTRAP_CODE=%d\n", trap_label, signal_info.si_code);
    printf("%s_HW_CLASSIFICATION=%s\n", trap_label,
           signal_info.si_code == TRAP_HWBKPT ? "YES" : "NO");
    if (signal_info.si_code != TRAP_HWBKPT) {
        return -1;
    }

    if (get_registers(context->tid, &registers, &register_length) != 0 ||
            register_length != sizeof(registers)) {
        print_errno("TRAP_GETREGSET_RESULT");
        return -1;
    }
    printf("%s_PC_MATCH=%s\n", trap_label,
           registers.pc == expected_pc ? "YES" : "NO");
    if (registers.pc != expected_pc) {
        return -1;
    }
    return 0;
}

static void terminate_tracee(struct trace_context *context) {
    int status = 0;

    if (!context->attached || !context->stopped) {
        return;
    }
    if (ptrace(PTRACE_KILL, context->tid, NULL, NULL) == 0) {
        (void) waitpid(context->tid, &status, __WALL);
        context->attached = 0;
        context->stopped = 0;
        puts("TRACE_TARGET_TERMINATION=REQUESTED");
    } else {
        print_errno("TRACE_TARGET_TERMINATION");
    }
}

static void cleanup_context(struct trace_context *context) {
    int status = 0;

    if (!context->attached) {
        return;
    }
    if (!context->stopped && stop_running_tracee(context, &status) != 0) {
        print_errno("CLEANUP_STOP_RESULT");
    }
    if (context->stopped && context->breakpoint_armed) {
        (void) disable_breakpoint(context, "FAILURE_BREAK_DISABLE_RESULT");
    }
    if (context->active_programming_started) {
        terminate_tracee(context);
        return;
    }
    if (context->stopped && ptrace(PTRACE_DETACH, context->tid, NULL, NULL) == 0) {
        context->attached = 0;
        context->stopped = 0;
        puts("PREACTIVE_DETACH_RESULT=SUCCESS");
    }
}

static int run_probe(int argc, char **argv) {
    struct trace_context context;
    uint64_t callsite;
    uint64_t plt;
    uint64_t got;
    uint64_t provider;
    uint64_t callsite_word0;
    uint64_t callsite_word1;
    uint64_t plt_word0;
    uint64_t plt_word1;
    uint64_t got_pointer;
    uint64_t provider_word0;
    uint64_t provider_word1;
    int result = 1;

    if (argc != 7 || parse_positive_pid(argv[2], &context.tid) != 0 ||
            parse_u64(argv[3], &callsite) != 0 ||
            parse_u64(argv[4], &plt) != 0 ||
            parse_u64(argv[5], &got) != 0 ||
            parse_u64(argv[6], &provider) != 0) {
        fprintf(stderr,
                "usage: phase16m_createdolphin_trace probe <tid> <callsite> "
                "<plt> <got> <provider>\n");
        return 2;
    }
    context.attached = 0;
    context.stopped = 0;
    context.active_programming_started = 0;
    context.breakpoint_armed = 0;
    context.breakpoint_address = 0;

    if (attach_and_validate(&context) != 0) {
        goto cleanup;
    }
    if (peek_u64(context.tid, callsite, &callsite_word0) != 0 ||
            peek_u64(context.tid, callsite + UINT64_C(8), &callsite_word1) != 0 ||
            peek_u64(context.tid, plt, &plt_word0) != 0 ||
            peek_u64(context.tid, plt + UINT64_C(8), &plt_word1) != 0 ||
            peek_u64(context.tid, got, &got_pointer) != 0 ||
            peek_u64(context.tid, provider, &provider_word0) != 0 ||
            peek_u64(context.tid, provider + UINT64_C(8),
                     &provider_word1) != 0) {
        print_errno("RUNTIME_READ_RESULT");
        goto cleanup;
    }
    printf("CALLSITE_WORD0=0x%016" PRIx64 "\n", callsite_word0);
    printf("CALLSITE_WORD1=0x%016" PRIx64 "\n", callsite_word1);
    printf("PLT_WORD0=0x%016" PRIx64 "\n", plt_word0);
    printf("PLT_WORD1=0x%016" PRIx64 "\n", plt_word1);
    printf("GOT_POINTER=0x%016" PRIx64 "\n", got_pointer);
    printf("GOT_POINTS_TO_PROVIDER=%s\n",
           got_pointer == provider ? "YES" : "NO");
    printf("PROVIDER_WORD0=0x%016" PRIx64 "\n", provider_word0);
    printf("PROVIDER_WORD1=0x%016" PRIx64 "\n", provider_word1);
    puts("PROBE_READS_COMPLETE=YES");

    if (ptrace(PTRACE_DETACH, context.tid, NULL, NULL) != 0) {
        print_errno("PROBE_DETACH_RESULT");
        goto cleanup;
    }
    context.attached = 0;
    context.stopped = 0;
    puts("PROBE_DETACH_RESULT=SUCCESS");
    result = 0;

cleanup:
    cleanup_context(&context);
    return result;
}

static int single_step_and_capture(
        struct trace_context *context,
        unsigned int step,
        struct user_pt_regs *registers) {
    enum bounded_wait_result wait_result;
    size_t register_length = 0;
    int status = 0;

    if (ptrace(PTRACE_SINGLESTEP, context->tid, NULL, NULL) != 0) {
        print_errno("SINGLESTEP_RESULT");
        return -1;
    }
    context->stopped = 0;
    wait_result = bounded_waitpid(context->tid, &status, STEP_TIMEOUT_MS);
    if (wait_result != BOUNDED_WAIT_EVENT || !WIFSTOPPED(status)) {
        printf("STEP%u_WAIT_RESULT=%s\n", step,
               wait_result == BOUNDED_WAIT_TIMEOUT ? "TIMEOUT" : "FAILURE");
        if (wait_result == BOUNDED_WAIT_TIMEOUT) {
            (void) stop_running_tracee(context, &status);
        }
        return -1;
    }
    context->stopped = 1;
    printf("STEP%u_STOP_SIGNAL=%d\n", step, WSTOPSIG(status));
    if (WSTOPSIG(status) != SIGTRAP ||
            get_registers(context->tid, registers, &register_length) != 0 ||
            register_length != sizeof(*registers)) {
        return -1;
    }
    printf("STEP%u_PC=0x%016" PRIx64 "\n", step,
           (uint64_t) registers->pc);
    printf("STEP%u_X0=0x%016" PRIx64 "\n", step,
           (uint64_t) registers->regs[0]);
    printf("STEP%u_X1=0x%016" PRIx64 "\n", step,
           (uint64_t) registers->regs[1]);
    printf("STEP%u_X2=0x%016" PRIx64 "\n", step,
           (uint64_t) registers->regs[2]);
    return 0;
}

static int run_observe(int argc, char **argv) {
    struct trace_context context;
    uint64_t callsite;
    uint64_t plt;
    uint64_t got;
    uint64_t provider;
    uint64_t expected_return;
    uint64_t expected_vptr;
    uint64_t callsite_word0;
    uint64_t callsite_word1;
    uint64_t plt_word0;
    uint64_t plt_word1;
    uint64_t got_pointer;
    uint64_t provider_word0;
    uint64_t provider_word1;
    uint64_t return_pointer;
    uint64_t object_vptr = 0;
    uint64_t object_word1 = 0;
    struct user_pt_regs entry_registers;
    struct user_pt_regs return_registers;
    struct user_pt_regs step_registers;
    size_t register_length = 0;
    uint64_t actual_return;
    int entry_timeout_ms;
    int status = 0;
    int wait_result;
    unsigned int step;
    int result = 1;

    memset(&context, 0, sizeof(context));
    if (argc != 10 || parse_positive_pid(argv[2], &context.tid) != 0 ||
            parse_u64(argv[3], &callsite) != 0 ||
            parse_u64(argv[4], &plt) != 0 ||
            parse_u64(argv[5], &got) != 0 ||
            parse_u64(argv[6], &provider) != 0 ||
            parse_u64(argv[7], &expected_return) != 0 ||
            parse_u64(argv[8], &expected_vptr) != 0 ||
            parse_timeout(argv[9], &entry_timeout_ms) != 0) {
        fprintf(stderr,
                "usage: phase16m_createdolphin_trace observe <tid> <callsite> "
                "<plt> <got> <provider> <expected_return> <expected_vptr> "
                "<entry_timeout_seconds>\n");
        return 2;
    }

    if (attach_and_validate(&context) != 0) {
        goto cleanup;
    }
    if (peek_u64(context.tid, callsite, &callsite_word0) != 0 ||
            peek_u64(context.tid, callsite + UINT64_C(8),
                     &callsite_word1) != 0 ||
            peek_u64(context.tid, plt, &plt_word0) != 0 ||
            peek_u64(context.tid, plt + UINT64_C(8), &plt_word1) != 0 ||
            peek_u64(context.tid, got, &got_pointer) != 0 ||
            peek_u64(context.tid, provider, &provider_word0) != 0 ||
            peek_u64(context.tid, provider + UINT64_C(8),
                     &provider_word1) != 0) {
        print_errno("FINAL_RUNTIME_READ_RESULT");
        goto cleanup;
    }
    printf("FINAL_CALLSITE_BYTES_MATCH=%s\n",
           callsite_word0 == EXPECTED_CALLSITE_WORD0 &&
                   callsite_word1 == EXPECTED_CALLSITE_WORD1 ? "YES" : "NO");
    printf("FINAL_PLT_BYTES_MATCH=%s\n",
           plt_word0 == EXPECTED_PLT_WORD0 &&
                   plt_word1 == EXPECTED_PLT_WORD1 ? "YES" : "NO");
    printf("FINAL_GOT_POINTS_TO_PROVIDER=%s\n",
           got_pointer == provider ? "YES" : "NO");
    printf("FINAL_PROVIDER_BYTES_MATCH=%s\n",
           provider_word0 == EXPECTED_PROVIDER_WORD0 &&
                   provider_word1 == EXPECTED_PROVIDER_WORD1 ? "YES" : "NO");
    if (callsite_word0 != EXPECTED_CALLSITE_WORD0 ||
            callsite_word1 != EXPECTED_CALLSITE_WORD1 ||
            plt_word0 != EXPECTED_PLT_WORD0 ||
            plt_word1 != EXPECTED_PLT_WORD1 ||
            got_pointer != provider ||
            provider_word0 != EXPECTED_PROVIDER_WORD0 ||
            provider_word1 != EXPECTED_PROVIDER_WORD1) {
        goto cleanup;
    }

    puts("OBSERVATION_COUNT=1");
    if (program_breakpoint(&context, provider,
                           "ENTRY_BREAK_SET_RESULT",
                           "ENTRY_ADDRESS_READBACK_MATCH",
                           "ENTRY_CTRL_MATCH") != 0) {
        goto cleanup;
    }
    wait_result = continue_to_hw_trap(&context, provider, entry_timeout_ms,
                                      "ENTRY_WAIT_RESULT",
                                      "ENTRY_TRAP", &status);
    if (wait_result != 0) {
        goto cleanup;
    }
    if (get_registers(context.tid, &entry_registers, &register_length) != 0 ||
            register_length != sizeof(entry_registers)) {
        print_errno("ENTRY_REGISTER_CAPTURE_RESULT");
        goto cleanup;
    }
    printf("ENTRY_PC=0x%016" PRIx64 "\n", (uint64_t) entry_registers.pc);
    printf("ENTRY_SP=0x%016" PRIx64 "\n", (uint64_t) entry_registers.sp);
    printf("ENTRY_LR=0x%016" PRIx64 "\n",
           (uint64_t) entry_registers.regs[30]);
    for (step = 0; step < 8U; step++) {
        printf("ENTRY_X%u=0x%016" PRIx64 "\n", step,
               (uint64_t) entry_registers.regs[step]);
    }
    actual_return = entry_registers.regs[30];
    printf("CALLER_RETURN_ADDRESS_MATCH_STATIC_SITE=%s\n",
           actual_return == expected_return ? "YES" : "NO");
    if (actual_return == 0U || (actual_return & UINT64_C(3)) != 0U) {
        puts("ACTUAL_RETURN_ADDRESS_VALID=NO");
        goto cleanup;
    }
    puts("ACTUAL_RETURN_ADDRESS_VALID=YES");

    if (disable_breakpoint(&context, "ENTRY_BREAK_DISABLE_RESULT") != 0) {
        goto cleanup;
    }
    if (program_breakpoint(&context, actual_return,
                           "RETURN_BREAK_SET_RESULT",
                           "RETURN_BREAK_ADDRESS_MATCH",
                           "RETURN_BREAK_CTRL_MATCH") != 0) {
        goto cleanup;
    }
    wait_result = continue_to_hw_trap(&context, actual_return,
                                      RETURN_TIMEOUT_MS,
                                      "RETURN_WAIT_RESULT",
                                      "RETURN_TRAP", &status);
    if (wait_result != 0) {
        goto cleanup;
    }
    if (get_registers(context.tid, &return_registers, &register_length) != 0 ||
            register_length != sizeof(return_registers)) {
        print_errno("RETURN_REGISTER_CAPTURE_RESULT");
        goto cleanup;
    }
    return_pointer = return_registers.regs[0];
    printf("RETURN_PC=0x%016" PRIx64 "\n", (uint64_t) return_registers.pc);
    printf("RETURN_X0=0x%016" PRIx64 "\n", return_pointer);
    printf("RETURN_POINTER_NON_NULL=%s\n",
           return_pointer != 0U ? "YES" : "NO");
    if (return_pointer != 0U) {
        if (peek_u64(context.tid, return_pointer, &object_vptr) != 0 ||
                peek_u64(context.tid, return_pointer + UINT64_C(8),
                         &object_word1) != 0) {
            print_errno("RETURN_OBJECT_READ_RESULT");
            goto cleanup;
        }
        printf("RETURN_OBJECT_VPTR=0x%016" PRIx64 "\n", object_vptr);
        printf("RETURN_OBJECT_WORD1=0x%016" PRIx64 "\n", object_word1);
        printf("RETURN_OBJECT_VPTR_MATCH=%s\n",
               object_vptr == expected_vptr ? "YES" : "NO");
    }

    if (disable_breakpoint(&context, "RETURN_BREAK_DISABLE_RESULT") != 0) {
        goto cleanup;
    }
    puts("IMMEDIATE_SINGLESTEP_USED=YES");
    for (step = 1U; step <= MAX_IMMEDIATE_STEPS; step++) {
        if (single_step_and_capture(&context, step, &step_registers) != 0) {
            goto cleanup;
        }
    }
    puts("IMMEDIATE_STEP_COUNT=3");
    puts("PHASE16M_SCOPE_STOP_REACHED=YES");

    terminate_tracee(&context);
    if (context.attached) {
        goto cleanup;
    }
    result = 0;

cleanup:
    cleanup_context(&context);
    printf("BREAKPOINT_DISABLED_BEFORE_EXIT=%s\n",
           context.breakpoint_armed ? "NO" : "YES_OR_NOT_ARMED");
    fflush(stdout);
    return result;
}

int main(int argc, char **argv) {
    if (argc < 2) {
        fprintf(stderr, "mode required: probe or observe\n");
        return 2;
    }
    if (strcmp(argv[1], "probe") == 0) {
        return run_probe(argc, argv);
    }
    if (strcmp(argv[1], "observe") == 0) {
        return run_observe(argc, argv);
    }
    fprintf(stderr, "unknown mode\n");
    return 2;
}
