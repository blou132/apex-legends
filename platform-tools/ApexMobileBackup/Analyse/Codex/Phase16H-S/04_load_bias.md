# Load-bias resolution

Host program headers identify the executable `PT_LOAD` at file offset `0x8a0`,
virtual address `0x18a0`, aligned to `0x1000`. The runtime executable mapping
was correlated with this page-aligned segment, not treated as a naive first-map
plus symbol calculation.

The symbol's corresponding file offset is `0x9a0`. Its four bytes decode to
`0xd10103ff`, matching the expected first instruction. For each disposable
process, the computed runtime address was four-byte aligned, inside the tracee
RX mapping, and confirmed against the same deployed binary hash.

```text
LOAD_BIAS_RESOLVED = YES
TRACE_TARGET_RUNTIME_VALIDATED = YES
```

ASLR addresses and raw maps are intentionally not committed.
