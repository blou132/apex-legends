# Phase16H-S scope

Date: 2026-08-30

This phase tested function-entry breakpoint capability only on disposable
ARM64 programs running on the rooted PRA-LX1. It did not install or launch
Apex, access Samsung hardware, modify Android or the kernel, weaken SELinux,
change Magisk, intercept traffic, or contact a backend.

One software-breakpoint write was attempted. It failed without changing
memory. The authorized hardware fallback was then tested on disposable
processes and stopped before execution because register-state readback and
restoration could not be validated.

Raw maps, runtime addresses, PIDs, logs, binaries, and downloaded kernel source
remain local-only under the gitignored `Analyse/LocalInputs/Phase16H-S` tree.
