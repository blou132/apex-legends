# Exit and detach cleanup semantics

## Thread exit and exec

PRA `flush_ptrace_hw_breakpoint()` unregisters every breakpoint/watchpoint and
nulls each thread pointer (`arch/arm64/kernel/ptrace.c:114-130`).

`do_exit()` calls this function at `kernel/exit.c:772`; `flush_thread()` calls
it at `arch/arm64/kernel/process.c:308`. Unregistering releases the perf event,
which removes an active event from its context and reaches PMU delete and the
architecture uninstall path.

Exact C33 KALLSYMS/disassembly confirms direct calls:

```text
do_exit -> flush_ptrace_hw_breakpoint
flush_thread -> flush_ptrace_hw_breakpoint
flush_ptrace_hw_breakpoint -> unregister_hw_breakpoint
```

## PTRACE_DETACH

Detach is different. PRA `ptrace_detach()` invokes `ptrace_disable()`, and the
ARM64 implementation only calls `user_disable_single_step()`
(`arch/arm64/kernel/ptrace.c:56-67`). It does not call
`flush_ptrace_hw_breakpoint()`.

Exact C33 disassembly independently confirms that `ptrace_disable` contains
only the single-step disable call. Therefore detach alone is not an acceptable
breakpoint cleanup mechanism on this kernel. A future test must disable the
hardware event successfully before detach.

```text
EXIT_CLEANUP_PATH_CONFIDENCE = HIGH
DETACH_CLEANUP_PATH_CONFIDENCE = HIGH_NEGATIVE_DETACH_ALONE_DOES_NOT_FLUSH
```
