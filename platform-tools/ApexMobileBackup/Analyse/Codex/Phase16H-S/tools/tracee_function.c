#include <inttypes.h>
#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <time.h>

static volatile sig_atomic_t keep_running = 1;

static void handle_signal(int signal_number) {
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

int main(void) {
    const struct timespec delay = {.tv_sec = 0, .tv_nsec = 100000000};
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
    const uint64_t expected = UINT64_C(0x26664);
    uint64_t iteration = 0;

    signal(SIGINT, handle_signal);
    signal(SIGTERM, handle_signal);

    puts("TRACEE_READY");
    fflush(stdout);

    while (keep_running) {
        uint64_t result = trace_target(
            values[0], values[1], values[2], values[3],
            values[4], values[5], values[6], values[7]);

        if (result != expected) {
            printf("TRACE_RESULT_FAILURE iteration=%" PRIu64
                   " actual=0x%" PRIx64 " expected=0x%" PRIx64 "\n",
                   iteration, result, expected);
            fflush(stdout);
            return 1;
        }

        if ((iteration % 10U) == 0U) {
            printf("TRACE_RESULT_OK iteration=%" PRIu64
                   " value=0x%" PRIx64 "\n", iteration, result);
            fflush(stdout);
        }

        iteration++;
        nanosleep(&delay, NULL);
    }

    puts("TRACEE_STOPPED");
    return 0;
}
