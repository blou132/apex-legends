# Reference recovery

Normal string xrefs are absent. The bounded recovery pass used aligned pointer
searches, relocation values, and exact AArch64 page-plus-low-offset pairs.

Results:

- aligned pointers to the four anchors: none;
- `.rela.dyn` entries examined: `1,435,206`;
- relocations matching anchor addresses: none;
- title exact `ADRP+ADD`: Ghidra `0x59ef9b0-0x59ef9b4` in
  `FUN_059ef114`;
- message lines: nearby page materializations in the same function, with the
  builder selecting complete neighboring strings rather than each minimal
  substring address.

```text
TITLE_POINTER_CHAIN = NOT_FOUND_AS_DATA_CHAIN; CONFIRMED_DIRECT_CODE_MATERIALIZATION
MESSAGE_POINTER_CHAIN = NOT_FOUND_AS_DATA_CHAIN; CONFIRMED_FUNCTION_LOCAL_CONSTRUCTION
```

Broad page-only hits were treated as ambiguous and were not used as xrefs.
