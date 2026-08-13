# Waits and conditions

No `pthread_cond_wait`, mutex, semaphore, futex wrapper, event wait, sleep loop,
or polling loop can be attributed to `nativeResumeMainInit` because its native
function body is unresolved.

| Wait function | Wait object | Precondition | Producer | Timeout | Blocking |
|---|---|---|---|---|---|
| `UNKNOWN` | `UNKNOWN` | `UNKNOWN` | `UNKNOWN` | `UNKNOWN` | `UNKNOWN` |

```text
FIRST_POSSIBLE_BLOCKING_WAIT = UNKNOWN
```
