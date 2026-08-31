# Entry breakpoint

The Phase16M tracer built as AArch64 PIE with official NDK r27d, API 26,
warnings as errors, and a matching host/device hash. Source review confirms
that its only state write targets `NT_ARM_HW_BREAK`. It contains no general
register write, process-memory write, software breakpoint, injection, or
remote mapping operation.

The first read-only probe failed at `PTRACE_ATTACH` with `EPERM`. On the fresh
final launch, the watcher detected the existing client tracer before invoking
the observation mode. Consequently no `PTRACE_SETREGSET` was executed and the
authorized active attempt was not consumed.

```text
TRACER_BUILD_VALID = YES
TRACER_REGISTER_WRITES = NO
TRACER_PROCESS_MEMORY_WRITES = NO
PTRACE_ATTACH_RESULT = FAILURE_PREACTIVE_EPERM
GENERAL_REGSET_VALID = NOT_READ
HW_REGSET_VALID = NOT_READ
HW_INITIAL_STATE_CLEAR = NOT_READ
PHASE16M_ACTIVE_READY = NO
ACTIVE_OBSERVATION_EXECUTED = NO
OBSERVATION_COUNT = 0
ENTRY_BREAK_SET_RESULT = NOT_EXECUTED
```
