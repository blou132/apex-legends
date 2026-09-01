# Owner field writes

Within the bounded readable owner lifecycle, the only proven write to
`DolphinUpdater+0x1f0` is the shutdown clear at Ghidra `0x04812ee0`. No
provenanced non-null writer is visible.

The beginning of the exact `CheckUpdate` region is protected or statically
opaque; the readable continuation already consumes a non-null field. The
unusable broad displacement-only scan was not used as evidence.

```text
DOLPHIN_OWNER_FIELD_WRITE_COUNT = 1_PROVEN_CLEAR
DOLPHIN_OWNER_FIELD_NON_NULL_WRITERS = 0_PROVEN
```
