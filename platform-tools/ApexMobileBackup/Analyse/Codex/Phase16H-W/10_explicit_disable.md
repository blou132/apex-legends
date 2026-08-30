# Explicit disable

While stopped at the validated function-entry trap, the tracer submitted the
mandatory separate slot-0 SETREGSET with the same address and control zero.
The syscall succeeded.

Diagnostic GETREGSET remained non-authoritative, as designed. It retained the
address and returned cached control `0x000041e5`; neither value was used as the
disable success criterion. The subsequent no-retrap test supplied the
functional proof.

- `HW_BREAK_DISABLE_SET_RESULT = SUCCESS`
- `DISABLE_READBACK_DIAGNOSTIC_ONLY = YES`
- `DISABLE_ADDRESS_READBACK_REQUIRED = NO`
