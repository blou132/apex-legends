# No-retrap validation

The tracee gate remained absent and its log contained only `TRACEE_READY`.
Neither the first target call nor the post-disable handshake ran.

The no-retrap result cannot be promoted from a path that never armed or
continued the breakpoint.

```text
POST_DISABLE_FUNCTION_EXECUTION_OBSERVED = NO_NOT_REACHED
POST_DISABLE_RETRAP = NOT_APPLICABLE_NOT_CONTINUED
```
