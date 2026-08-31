# Bounded backtrace

A backtrace was not required. Direct same-run log correlation resolves the
historical executing thread more strongly than a later sampled stack would.

```text
BOUNDED_BACKTRACE_USED = NO
BACKTRACE_THREAD_ROLE_EVIDENCE = NOT_REQUIRED
```
