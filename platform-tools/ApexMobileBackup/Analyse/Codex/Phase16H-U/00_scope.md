# Phase16H-U scope

Phase16H-U authorized one bounded ARM64 hardware execution-breakpoint attempt
on one disposable `trace_target` process on the rooted PRA-LX1.

The attempt could use `PTRACE_ATTACH`, `NT_ARM_HW_BREAK`, `PTRACE_CONT`, and
read-only register capture. It could not use software breakpoints, memory
patching, SELinux changes, persistent services, Apex, Samsung, or network work.

The single attempt was consumed. It stopped at address readback before the
active control was programmed. No second attempt was made.

Raw maps, absolute runtime addresses, binaries, and runtime logs remain under
gitignored `Analyse/LocalInputs/Phase16H-U/`.
