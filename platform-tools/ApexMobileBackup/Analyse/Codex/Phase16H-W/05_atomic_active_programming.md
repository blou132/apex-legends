# Atomic active programming

All final gates were rechecked before invoking the tracer. The single runtime
attempt was then consumed by exactly one active SETREGSET submission:

```text
slot = 0
address = freshly resolved trace_target runtime address
control = 0x000001e5
iov_len = 24
```

No disabled pre-step, split address/control operation, second slot, software
breakpoint, or retry occurred.

- `ACTIVE_PROGRAMMING_READY = YES`
- `RUNTIME_ATTEMPT_COUNT = 1`
- `RUNTIME_ATTEMPT_LIMIT = ONE`
- `ACTIVE_SETREGSET_RESULT = SUCCESS`
