# Root capability boundary

Generic checks only were used. Root can read `/proc/1/maps`. `debuggerd` is
present; `showmap`, `strace`, and `gdbserver` were not found through the tested
shell path. Yama `ptrace_scope` is unavailable and `perf_event_paranoid` is 3.
SELinux remains enforcing.

No process was attached, traced, signaled, dumped, hooked, or instrumented.
Root establishes a useful prerequisite but does not by itself prove a future
native callback trace workflow.

```text
ROOT_PROC_MAPS_CAPABILITY = YES
ROOT_NATIVE_TRACE_PRECONDITION = PROBABLE
ROOT_CALLBACK_TRACE_PRECONDITION = PROBABLE
PROCESS_ATTACHMENT_PERFORMED = NO
```
