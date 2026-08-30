# No-retrap proof

After explicit disable, the tracee was continued. It completed the originally
trapped call, completed a second `trace_target` call with the expected
deterministic result, and reached its controlled SIGSTOP handshake.

The observed stop was SIGSTOP, not SIGTRAP. This proves the breakpoint was not
retriggered on the next function entry.

- `POST_DISABLE_FUNCTION_EXECUTION_OBSERVED = YES`
- `POST_DISABLE_RESULT_CORRECT = YES`
- `POST_DISABLE_RETRAP = NO`
- `POST_DISABLE_HANDSHAKE_REACHED = YES`
- `HW_BREAK_DISABLED_BY_PROVEN_PATH = YES`
