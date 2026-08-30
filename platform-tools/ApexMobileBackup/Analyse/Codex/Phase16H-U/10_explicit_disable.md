# Explicit disable result

The active control `0x000001e5` was never submitted. The only slot submission
used disabled control zero, so there was no armed breakpoint requiring the
runtime disable sequence.

On the readback mismatch, the tracer used `PTRACE_KILL` on the stopped
disposable tracee. Thread exit is the high-confidence final cleanup boundary
established in Phase16H-T. The tracer did not detach from uncertain state.

```text
HW_BREAK_DISABLE_SET_RESULT = NOT_REQUIRED_ACTIVE_CONTROL_NEVER_SET
HW_BREAK_DISABLED_BY_PROVEN_PATH = NOT_APPLICABLE_THREAD_EXIT_CLEANUP
BREAKPOINT_DISABLED_BEFORE_EXIT = NO_NOT_ARMED
```
