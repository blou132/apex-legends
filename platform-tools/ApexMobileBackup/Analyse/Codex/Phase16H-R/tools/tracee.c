#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <time.h>
#include <unistd.h>

static volatile sig_atomic_t keep_running = 1;
static volatile uint64_t heartbeat = 0;

static void handle_signal(int signal_number) {
    (void) signal_number;
    keep_running = 0;
}

int main(void) {
    const struct timespec delay = {.tv_sec = 0, .tv_nsec = 100000000};

    signal(SIGINT, handle_signal);
    signal(SIGTERM, handle_signal);
    puts("TRACEE_READY");
    fflush(stdout);

    while (keep_running) {
        heartbeat++;
        nanosleep(&delay, NULL);
    }

    puts("TRACEE_STOPPED");
    return 0;
}
