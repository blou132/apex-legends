#include <asm/ptrace.h>
#include <errno.h>
#include <linux/elf.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/uio.h>
#include <sys/wait.h>

static void print_failure(const char *operation) {
    printf("%s=FAILURE errno=%d message=%s\n", operation, errno, strerror(errno));
}

int main(int argc, char **argv) {
    char *end = NULL;
    long parsed_pid;
    pid_t target_pid;
    int status = 0;
    int result = 1;
    int attached = 0;
    struct user_pt_regs registers = {0};
    struct iovec register_set = {
        .iov_base = &registers,
        .iov_len = sizeof(registers),
    };

    if (argc != 2) {
        fprintf(stderr, "usage: ptrace_probe <pid>\n");
        return 2;
    }

    errno = 0;
    parsed_pid = strtol(argv[1], &end, 10);
    if (errno != 0 || end == argv[1] || *end != '\0' || parsed_pid <= 0) {
        fprintf(stderr, "invalid pid\n");
        return 2;
    }
    target_pid = (pid_t) parsed_pid;

    if (ptrace(PTRACE_ATTACH, target_pid, NULL, NULL) == -1) {
        print_failure("PTRACE_ATTACH_RESULT");
        return 1;
    }
    attached = 1;
    puts("PTRACE_ATTACH_RESULT=SUCCESS");

    if (waitpid(target_pid, &status, 0) == -1) {
        print_failure("PTRACE_WAIT_STOP_RESULT");
        goto cleanup;
    }
    if (!WIFSTOPPED(status)) {
        printf("PTRACE_WAIT_STOP_RESULT=FAILURE unexpected_status=%d\n", status);
        goto cleanup;
    }
    printf("PTRACE_WAIT_STOP_RESULT=SUCCESS stop_signal=%d\n", WSTOPSIG(status));

    if (ptrace(PTRACE_GETREGSET, target_pid, (void *) NT_PRSTATUS, &register_set) == -1) {
        print_failure("PTRACE_GETREGSET_RESULT");
        goto cleanup;
    }
    printf("PTRACE_GETREGSET_RESULT=SUCCESS bytes=%zu\n", register_set.iov_len);
    result = 0;

cleanup:
    if (attached) {
        if (ptrace(PTRACE_DETACH, target_pid, NULL, NULL) == -1) {
            print_failure("PTRACE_DETACH_RESULT");
            result = 1;
        } else {
            puts("PTRACE_DETACH_RESULT=SUCCESS");
        }
    }
    return result;
}
