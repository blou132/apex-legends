# Ptrace breakpoint design

The software probe performs bounded `PTRACE_ATTACH`, stopped-state wait,
`NT_PRSTATUS` validation, `PTRACE_PEEKTEXT`, one safe aligned word
read-modify-write attempt, `PTRACE_CONT`, trap inspection, exact restoration,
one `PTRACE_SINGLESTEP`, and detach.

The BRK encoding is `0xd4200000`. The probe verifies the local expected first
instruction before any write. If restoration cannot be verified, it never
resumes or detaches the target and instead terminates the disposable process.

The hardware probe follows the Android 4.4 ARM64 UAPI for
`user_hwdebug_state`, queries `NT_ARM_HW_BREAK`, requires an unused slot, and
touches only slot zero. It similarly terminates the disposable process if the
initial slot state cannot be restored exactly.
