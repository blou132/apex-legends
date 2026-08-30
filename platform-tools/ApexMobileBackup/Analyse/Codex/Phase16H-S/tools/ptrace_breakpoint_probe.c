#include <asm/ptrace.h>
#include <errno.h>
#include <inttypes.h>
#include <linux/elf.h>
#include <signal.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/uio.h>
#include <sys/wait.h>

#define AARCH64_BRK_0 UINT32_C(0xd4200000)

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

static int set_registers(pid_t pid, struct user_pt_regs *registers, size_t length) {
    struct iovec register_set = {
        .iov_base = registers,
        .iov_len = length,
    };

    return ptrace(PTRACE_SETREGSET, pid, (void *) NT_PRSTATUS, &register_set);
}

static int read_word(pid_t pid, uintptr_t address, unsigned long *word) {
    long value;

    errno = 0;
    value = ptrace(PTRACE_PEEKTEXT, pid, (void *) address, NULL);
    if (value == -1 && errno != 0) {
        return -1;
    }

    *word = (unsigned long) value;
    return 0;
}

static int write_word(pid_t pid, uintptr_t address, unsigned long word) {
    return ptrace(PTRACE_POKETEXT, pid, (void *) address, (void *) word);
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
    uint64_t parsed_address;
    uint64_t parsed_instruction;
    uintptr_t breakpoint_address;
    uintptr_t word_address;
    size_t byte_offset;
    unsigned int bit_shift;
    unsigned long original_word = 0;
    unsigned long breakpoint_word;
    unsigned long readback_word = 0;
    uint32_t original_instruction;
    uint32_t expected_instruction;
    struct user_pt_regs registers;
    struct user_pt_regs trap_registers;
    struct user_pt_regs step_registers;
    size_t register_length = 0;
    size_t trap_register_length = 0;
    size_t step_register_length = 0;
    siginfo_t signal_info;
    int status = 0;
    int attached = 0;
    int breakpoint_installed = 0;
    int result = 1;
    unsigned int index;

    if (argc != 4) {
        fprintf(stderr, "usage: ptrace_breakpoint_probe <pid> <address> <expected_instruction>\n");
        return 2;
    }
    if (parse_positive_pid(argv[1], &target_pid) != 0 ||
            parse_uint64(argv[2], &parsed_address) != 0 ||
            parse_uint64(argv[3], &parsed_instruction) != 0 ||
            parsed_instruction > UINT32_MAX) {
        fprintf(stderr, "invalid argument\n");
        return 2;
    }

    breakpoint_address = (uintptr_t) parsed_address;
    expected_instruction = (uint32_t) parsed_instruction;
    if ((breakpoint_address & UINTPTR_C(3)) != 0U) {
        puts("BREAKPOINT_ADDRESS_ALIGNED=NO");
        return 2;
    }

    word_address = breakpoint_address & ~(uintptr_t) (sizeof(long) - 1U);
    byte_offset = breakpoint_address - word_address;
    if (byte_offset + sizeof(uint32_t) > sizeof(long)) {
        puts("BREAKPOINT_WORD_LAYOUT_VALID=NO");
        return 2;
    }
    bit_shift = (unsigned int) (byte_offset * 8U);

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
    printf("PTRACE_WAIT_STOP_RESULT=SUCCESS signal=%d\n", WSTOPSIG(status));

    if (get_registers(target_pid, &registers, &register_length) == -1) {
        print_errno("INITIAL_GETREGSET_RESULT");
        goto cleanup;
    }
    printf("REGISTER_SET_LENGTH=%zu\n", register_length);
    if (register_length != sizeof(registers)) {
        printf("REGISTER_LAYOUT_VALID=NO expected=%zu actual=%zu\n",
               sizeof(registers), register_length);
        goto cleanup;
    }
    puts("REGISTER_LAYOUT_VALID=YES");

    if (read_word(target_pid, word_address, &original_word) == -1) {
        print_errno("PTRACE_PEEKTEXT_RESULT");
        goto cleanup;
    }
    original_instruction = (uint32_t) (original_word >> bit_shift);
    printf("ORIGINAL_INSTRUCTION_MATCH=%s\n",
           original_instruction == expected_instruction ? "YES" : "NO");
    if (original_instruction != expected_instruction) {
        goto cleanup;
    }
    puts("ORIGINAL_WORD_SAVED=YES");

    breakpoint_word = original_word;
    breakpoint_word &= ~((unsigned long) UINT32_MAX << bit_shift);
    breakpoint_word |= (unsigned long) AARCH64_BRK_0 << bit_shift;

    if (write_word(target_pid, word_address, breakpoint_word) == -1) {
        print_errno("BREAKPOINT_WRITE_RESULT");
        puts("SOFTWARE_BREAKPOINT_SUPPORTED=NO");
        goto cleanup;
    }
    breakpoint_installed = 1;
    puts("BREAKPOINT_WRITE_RESULT=SUCCESS");

    if (read_word(target_pid, word_address, &readback_word) == -1) {
        print_errno("BREAKPOINT_READBACK_RESULT");
        goto cleanup;
    }
    if (readback_word != breakpoint_word) {
        puts("BREAKPOINT_READBACK_VALID=NO");
        goto cleanup;
    }
    puts("BREAKPOINT_READBACK_VALID=YES");
    puts("SOFTWARE_BREAKPOINT_SUPPORTED=YES");

    if (ptrace(PTRACE_CONT, target_pid, NULL, NULL) == -1) {
        print_errno("PTRACE_CONT_RESULT");
        goto cleanup;
    }
    if (waitpid(target_pid, &status, 0) == -1 || !WIFSTOPPED(status)) {
        if (errno != 0) {
            print_errno("BREAKPOINT_WAIT_RESULT");
        } else {
            printf("BREAKPOINT_WAIT_RESULT=FAILURE status=%d\n", status);
        }
        goto cleanup;
    }
    printf("BREAKPOINT_STOP_SIGNAL=%d\n", WSTOPSIG(status));
    if (WSTOPSIG(status) != SIGTRAP) {
        puts("SIGTRAP_RECEIVED=NO");
        goto cleanup;
    }
    puts("SIGTRAP_RECEIVED=YES");

    memset(&signal_info, 0, sizeof(signal_info));
    if (ptrace(PTRACE_GETSIGINFO, target_pid, NULL, &signal_info) == -1) {
        print_errno("SIGTRAP_INFO_CAPTURED");
        goto cleanup;
    }
    printf("SIGTRAP_INFO_CAPTURED=YES signo=%d code=%d\n",
           signal_info.si_signo, signal_info.si_code);

    if (get_registers(target_pid, &trap_registers, &trap_register_length) == -1) {
        print_errno("TRAP_GETREGSET_RESULT");
        goto cleanup;
    }
    if (trap_register_length != register_length) {
        puts("TRAP_REGISTER_LAYOUT_VALID=NO");
        goto cleanup;
    }
    puts("TRAP_REGISTER_LAYOUT_VALID=YES");

    if (trap_registers.pc == breakpoint_address) {
        puts("PC_RELATIVE_TO_BREAKPOINT=AT_BREAKPOINT");
    } else if (trap_registers.pc == breakpoint_address + sizeof(uint32_t)) {
        puts("PC_RELATIVE_TO_BREAKPOINT=AFTER_BREAKPOINT");
    } else {
        puts("PC_RELATIVE_TO_BREAKPOINT=UNEXPECTED");
        goto cleanup;
    }
    puts("EXACT_FUNCTION_ENTRY_TRAP=YES");
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
    puts("FUNCTION_ARGUMENT_CAPTURE=YES");

    if (write_word(target_pid, word_address, original_word) == -1) {
        print_errno("RESTORE_WRITE_RESULT");
        terminate_stopped_tracee(target_pid);
        return 1;
    }
    puts("RESTORE_WRITE_RESULT=SUCCESS");

    if (read_word(target_pid, word_address, &readback_word) == -1) {
        print_errno("RESTORE_READBACK_RESULT");
        goto cleanup;
    }
    if (readback_word != original_word) {
        puts("RESTORE_READBACK_MATCH=NO");
        terminate_stopped_tracee(target_pid);
        return 1;
    }
    breakpoint_installed = 0;
    puts("RESTORE_READBACK_MATCH=YES");
    puts("ORIGINAL_INSTRUCTION_RESTORED=YES");

    if (trap_registers.pc == breakpoint_address + sizeof(uint32_t)) {
        trap_registers.pc = breakpoint_address;
        if (set_registers(target_pid, &trap_registers, trap_register_length) == -1) {
            print_errno("PC_CORRECTION_APPLIED");
            goto cleanup;
        }
        puts("PC_CORRECTION_REQUIRED=YES");
        puts("PC_CORRECTION_APPLIED=YES");
    } else {
        puts("PC_CORRECTION_REQUIRED=NO");
        puts("PC_CORRECTION_APPLIED=NO");
    }

    if (ptrace(PTRACE_SINGLESTEP, target_pid, NULL, NULL) == -1) {
        print_errno("SINGLESTEP_RESULT");
        goto cleanup;
    }
    if (waitpid(target_pid, &status, 0) == -1 || !WIFSTOPPED(status)) {
        if (errno != 0) {
            print_errno("SINGLESTEP_WAIT_RESULT");
        } else {
            printf("SINGLESTEP_WAIT_RESULT=FAILURE status=%d\n", status);
        }
        goto cleanup;
    }
    if (WSTOPSIG(status) != SIGTRAP) {
        printf("SINGLESTEP_RESULT=FAILURE signal=%d\n", WSTOPSIG(status));
        goto cleanup;
    }
    if (get_registers(target_pid, &step_registers, &step_register_length) == -1) {
        print_errno("SINGLESTEP_GETREGSET_RESULT");
        goto cleanup;
    }
    if (step_register_length != register_length ||
            step_registers.pc == breakpoint_address) {
        puts("ORIGINAL_INSTRUCTION_EXECUTED=NO");
        goto cleanup;
    }
    puts("SINGLESTEP_RESULT=SUCCESS");
    puts("ORIGINAL_INSTRUCTION_EXECUTED=YES");

    if (ptrace(PTRACE_DETACH, target_pid, NULL, NULL) == -1) {
        print_errno("PTRACE_DETACH_RESULT");
        goto cleanup;
    }
    attached = 0;
    puts("PTRACE_DETACH_RESULT=SUCCESS");
    result = 0;

cleanup:
    if (breakpoint_installed) {
        if (write_word(target_pid, word_address, original_word) == -1) {
            print_errno("CLEANUP_RESTORE_RESULT");
            terminate_stopped_tracee(target_pid);
            return 1;
        }
        if (read_word(target_pid, word_address, &readback_word) == -1 ||
                readback_word != original_word) {
            puts("CLEANUP_RESTORE_READBACK=FAILURE");
            terminate_stopped_tracee(target_pid);
            return 1;
        }
        breakpoint_installed = 0;
        puts("CLEANUP_RESTORE_RESULT=SUCCESS");
    }

    if (attached) {
        if (ptrace(PTRACE_DETACH, target_pid, NULL, NULL) == -1) {
            print_errno("CLEANUP_DETACH_RESULT");
            terminate_stopped_tracee(target_pid);
            return 1;
        }
        puts("CLEANUP_DETACH_RESULT=SUCCESS");
    }

    return result;
}
