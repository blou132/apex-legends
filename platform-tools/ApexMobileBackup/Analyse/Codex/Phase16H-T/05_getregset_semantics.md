# GETREGSET semantics

PRA `hw_break_get()` calls `ptrace_hbp_get_ctrl()`, which returns:

```c
encode_ctrl_reg(counter_arch_bp(bp)->ctrl)
```

This is the cached `struct arch_hw_breakpoint` inside the perf event. It does
not call `read_wb_reg()` and does not read `DBGBCR<n>_EL1`.

Relevant PRA paths:

- `arch/arm64/kernel/ptrace.c:282-307`
- `arch/arm64/kernel/ptrace.c:380-430`

Lineage uses the same cached-control expression at lines 384-409. Exact C33
disassembly of `ptrace_hbp_get_ctrl` loads the cached mask, SSC, length, type,
privilege, and enabled fields and re-encodes them directly.

Consequences:

- `0x41e4` is a perf cached-state serialization, not a hardware BCR read.
- The cached enabled bit can remain zero while the perf event is enabled.
- A full zero word is not required after disable because type, length,
  privilege, and SSC can remain cached.

```text
GETREGSET_CTRL_SOURCE = CACHED_PERF_STATE
```
