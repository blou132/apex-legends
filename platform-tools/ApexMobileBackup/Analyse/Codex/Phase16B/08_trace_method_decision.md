# Trace method decision

No available supported mechanism can produce a native target address, native
call stack, callback identity, register argument, vtable address, or indirect
call destination for the installed package.

```text
ROOT_REQUIRED_FOR_USEFUL_TRACE = YES
NONINVASIVE_RUNTIME_TRACE_AVAILABLE = NO
BEST_FUTURE_TRACE_METHOD = NO_NONINVASIVE_METHOD
FINAL_GATE = D NO_NONINVASIVE_RUNTIME_TRACE_AVAILABLE
```

This result does not authorize root. Per the stop rule, no escalation,
bootloader action, attachment, injection, hook, patch, or profiler deployment
was attempted. `YES` applies specifically to obtaining the requested exact
native evidence from the current device and unchanged production package.
