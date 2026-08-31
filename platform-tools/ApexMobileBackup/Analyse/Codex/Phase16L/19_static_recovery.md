# Static recovery result

Phase16L resolves the complete machine edge:

```text
BL 0x05a314cc
  -> PLT 0x0a3c18f0
  -> GOT 0x0b37e8c8
  -> .rela.plt index 529
  -> dynsym index 375
  -> CreateDolphin
```

The single real callsite loses the returned pointer in a noreturn error path.
No owner or Init dispatch follows.

```text
STATIC_RECOVERY_LEVEL = C REAL_CREATEDOLPHIN_CALLSITE_RESOLVED_OBJECT_FLOW_LOST
```
