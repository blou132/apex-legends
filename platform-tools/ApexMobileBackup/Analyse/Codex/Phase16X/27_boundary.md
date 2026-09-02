# Boundary

The analysis stopped when exact selector references yielded no compatible
source-registry mutation. It did not escalate to:

- a global `+0x38` or `+0x08` displacement scan;
- generic UObject construction or whole-callgraph analysis;
- callback opaque-prefix, vtable writer, or dead CreateDolphin work;
- protector analysis or runtime tracing.

Raw Ghidra output, helper scripts, byte samples, and proprietary disassembly
remain local-only and gitignored. Committed files contain cleaned metadata.

```text
FUTURE_PTRACE_TRACE_GATE = NO_GO
```
