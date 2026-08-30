# Register capture

At the hardware trap, GETREGSET returned the expected 272-byte ARM64 general
register state. PC matched the exact `trace_target` runtime entry. SP and LR
were captured successfully.

Absolute PC, SP, LR, FP, and other raw register values remain local-only.

- `PC_CAPTURED = YES`
- `SP_CAPTURED = YES`
- `LR_CAPTURED = YES`
- `EXACT_FUNCTION_ENTRY_TRAP = YES`
