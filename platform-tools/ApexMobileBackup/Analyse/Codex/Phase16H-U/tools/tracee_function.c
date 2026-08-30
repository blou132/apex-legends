#include <errno.h>
#include <inttypes.h>
#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <time.h>
#include <unistd.h>

#define START_GATE_PATH "/data/local/tmp/phase16h_u_start_gate"

static volatile sig_atomic_t keep_running = 1;

static void handle_termination(int signal_number) {
    (void) signal_number;
    keep_running = 0;
}

__attribute__((noinline))
__attribute__((visibility("default")))
uint64_t trace_target(
        uint64_t a0,
        uint64_t a1,
        uint64_t a2,
        uint64_t a3,
        uint64_t a4,
        uint64_t a5,
        uint64_t a6,
        uint64_t a7) {
    return a0 + a1 + a2 + a3 + a4 + a5 + a6 + a7;
}

static uint64_t call_trace_target(const volatile uint64_t values[8]) {
    return trace_target(
        values[0], values[1], values[2], values[3],
        values[4], values[5], values[6], values[7]);
}

static int require_expected_result(const char *stage, uint64_t result) {
    const uint64_t expected = UINT64_C(0x26664);

    if (result != expected) {
        printf("%s=FAILURE actual=0x%" PRIx64 " expected=0x%" PRIx64 "\n",
               stage, result, expected);
        fflush(stdout);
        return -1;
    }

    printf("%s=SUCCESS value=0x%" PRIx64 "\n", stage, result);
    fflush(stdout);
    return 0;
}

int main(void) {
    const struct timespec gate_poll_delay = {.tv_sec = 0, .tv_nsec = 10000000};
    const struct timespec loop_delay = {.tv_sec = 0, .tv_nsec = 100000000};
    volatile uint64_t values[8] = {
        UINT64_C(0x1111),
        UINT64_C(0x2222),
        UINT64_C(0x3333),
        UINT64_C(0x4444),
        UINT64_C(0x5555),
        UINT64_C(0x6666),
        UINT64_C(0x7777),
        UINT64_C(0x8888),
    };
    uint64_t iteration = 0;

    signal(SIGINT, handle_termination);
    signal(SIGTERM, handle_termination);

    printf("TRACEE_READY pid=%d\n", getpid());
    fflush(stdout);

    while (keep_running && access(START_GATE_PATH, F_OK) != 0) {
        if (errno != ENOENT) {
            perror("START_GATE_WAIT");
            return 1;
        }
        nanosleep(&gate_poll_delay, NULL);
    }
    if (!keep_running) {
        puts("TRACEE_TERMINATED_CLEANLY=YES");
        return 0;
    }
    if (unlink(START_GATE_PATH) != 0) {
        perror("START_GATE_CONSUME");
        return 1;
    }
    puts("START_GATE_CONSUMED=YES");
    fflush(stdout);

    if (require_expected_result(
            "FIRST_TRACED_CALL_RESULT",
            call_trace_target(values)) != 0) {
        return 1;
    }

    if (require_expected_result(
            "POST_DISABLE_FUNCTION_EXECUTION",
            call_trace_target(values)) != 0) {
        return 1;
    }

    puts("POST_DISABLE_SIGSTOP_HANDSHAKE=READY");
    fflush(stdout);
    raise(SIGSTOP);

    if (require_expected_result(
            "POST_DETACH_FUNCTION_RESULT",
            call_trace_target(values)) != 0) {
        return 1;
    }

    while (keep_running) {
        uint64_t result = call_trace_target(values);

        if (result != UINT64_C(0x26664)) {
            return 1;
        }
        if ((iteration % 10U) == 0U) {
            printf("POST_DETACH_HEALTHY iteration=%" PRIu64 "\n", iteration);
            fflush(stdout);
        }
        iteration++;
        nanosleep(&loop_delay, NULL);
    }

    puts("TRACEE_TERMINATED_CLEANLY=YES");
    fflush(stdout);
    return 0;
}
