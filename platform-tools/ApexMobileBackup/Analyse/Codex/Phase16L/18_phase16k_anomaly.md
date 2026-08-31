# Phase16K anomaly resolution

Phase16K's PLT label was not shifted: raw branch decoding, PLT instructions,
GOT address, `.rela.plt`, and `.dynsym` all independently identify the direct
target as `CreateDolphin`.

The apparent one-entry mismatch in surrounding semantic operations is present
in the file bytes themselves. For example, the factory result is prepared as
`x1` for the next PLT entry's string-constructor shape, but the actual branch
targets the preceding `throw_length_error` entry. No `.rela.dyn` callsite
relocation corrects it. This proves an incoherent/noreturn static path, not a
Ghidra symbol shift. It does not prove why the producer emitted that path.

Phase16K was correct to invalidate the site as ownership, but its exact call
identity is now confirmed.

```text
PHASE16K_PLT_ANOMALY_ROOT_CAUSE = OTHER_PROVEN: NO_PLT_MISATTRIBUTION; REAL_CALL_RETURN_ENTERS_NORETURN_ERROR_PATH; PRODUCER_REASON_UNKNOWN
```
