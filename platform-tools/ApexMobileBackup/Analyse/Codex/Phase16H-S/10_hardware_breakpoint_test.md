# Hardware breakpoint test

The first bounded hardware attempt returned success from `PTRACE_SETREGSET`,
but strict readback did not match. Its initial cleanup order attempted address
restoration before disabling the active control and could not validate removal,
so the disposable tracee was terminated.

One corrective retry used a fresh tracee, static UAPI layout assertions,
readback diagnostics, and disable-first restoration. The runtime address read
back correctly, but the requested control `0x000001e5` returned as
`0x000041e4`: EL0/type/length fields were recognizable, the enable bit was
clear, and an unexplained vendor bit was set. Exact programming was therefore
not accepted. Disable-first restoration still could not be verified, and the
fresh stopped tracee was terminated safely.

No `PTRACE_CONT` occurred in either hardware attempt. There was no SIGTRAP,
register capture, detach with a programmed slot, system crash, or persistent
hardware-debug state; task exit removed the per-thread state.

```text
HW_BREAKPOINT_SETREGSET = SUCCESS
HW_BREAKPOINT_PROGRAMMED = NO_READBACK_INVALID
HW_BREAKPOINT_SIGTRAP = NO_NOT_CONTINUED
HW_FUNCTION_ARGUMENT_CAPTURE = NO_NOT_REACHED
HW_BREAKPOINT_REMOVED = NO_RESTORE_NOT_VALIDATED
HW_TRACEE_CONTINUES = NO_SAFETY_TERMINATED
```
