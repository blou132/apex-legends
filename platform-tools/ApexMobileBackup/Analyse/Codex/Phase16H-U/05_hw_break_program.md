# Hardware-breakpoint programming

To satisfy the requested address checkpoint, the tested tracer first submitted
slot 0 with the validated target address and disabled control `0`. The
`PTRACE_SETREGSET` call returned success.

The immediate `PTRACE_GETREGSET` address did not equal the submitted target.
The tracer therefore stopped before submitting active control `0x000001e5`.
The gate was never created and the tracee never called `trace_target`.

The exact observed address was not printed by this tracer revision. It is not
reconstructed or guessed in the report.

```text
HW_BREAK_ADDR_SET_RESULT = SUCCESS
HW_BREAK_ADDR_READBACK_MATCH = NO
HW_BREAK_CTRL_SET_RESULT = NOT_REACHED
HW_BREAKPOINT_ARMED_FOR_TEST = NO
```
