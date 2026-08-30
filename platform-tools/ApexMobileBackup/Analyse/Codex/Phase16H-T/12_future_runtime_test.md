# Future single runtime test design

This is design only. Phase16H-T does not execute it.

## Tracee contract

Use one disposable ARM64 tracee with a stable, non-inlined, four-byte-aligned
`trace_target`, known x0-x7 arguments, two deterministic calls, and a deliberate
SIGSTOP handshake after the second call. No Apex process and no software
breakpoint write are allowed.

## One bounded attempt

1. Revalidate target symbol, PT_LOAD mapping, load bias, first instruction, and
   clean initial hardware-breakpoint slots.
2. `PTRACE_ATTACH` and wait for the ptrace stop.
3. SET the exact target address in one execution slot.
4. SET control `0x000001e5`.
5. Require SETREGSET success and accept normalized cached GETREGSET
   `0x000041e4`; require exact address readback.
6. `PTRACE_CONT` once and require SIGTRAP at exact `trace_target`.
7. Capture PC, SP, LR, and x0-x7; require the eight known arguments.
8. While stopped, SET the slot control to zero. Require syscall success and
   enabled-bit readback zero, but do not require the entire cached word zero.
9. Continue. Require no second hardware SIGTRAP; the tracee must complete its
   second target call and reach its deliberate SIGSTOP handshake.
10. Detach only from that controlled stop. Require the tracee to finish with
    the deterministic result.
11. On any timeout or unexpected stop, terminate only the disposable tracee;
    thread exit is the high-confidence final hardware-state cleanup boundary.

## Stop conditions

- Any SETREGSET failure.
- Readback other than exact address plus normalized `0x41e4` before continue.
- Trap PC not equal to the target entry.
- Disable SETREGSET failure.
- A second hardware trap after disable.
- Missing controlled stop before detach.

```text
HW_BREAKPOINT_RETRY_GATE = GO
```
