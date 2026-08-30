# Runtime address validation

The deployed tracee hash matched the host build. The load bias was derived by
correlating the executable `PT_LOAD` page with the tracee RX mapping, rather
than treating the first mapping as the bias.

The runtime target was computed as load bias plus ELF symbol value `0x1c58`.
It was four-byte aligned, inside the tracee RX mapping, and associated with the
single-thread disposable tracee. Absolute addresses and raw maps are retained
local-only.

```text
TRACE_TARGET_RUNTIME_VALIDATED = YES
```
