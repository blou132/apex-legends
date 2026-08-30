# Apex tracer design

The tracer adapts the proven Phase16H-V/W atomic ARM64 hardware-breakpoint
state machine. It accepts one TID and a freshly observed module load bias, then
computes `load_bias + 0x00476180` in 64-bit C.

Before active programming it validates `NT_PRSTATUS` length 272, hardware-debug
regset length 264, six execution slots, all slots clear, and two read-only words
matching the exact function entry. Only then does it issue one atomic slot-0
address plus control `0x000001e5` operation.

At a valid trap it would capture registers and perform exactly three callback
reads: callback object from `*X1`, vptr from object `+0x00`, and target from
vptr `+0x28`. Cleanup tracks armed/stopped state and allows one explicit
disable before detach. Uncertain active state terminates the tracee instead of
detaching.
