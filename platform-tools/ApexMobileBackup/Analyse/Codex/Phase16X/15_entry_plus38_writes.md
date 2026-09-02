# Entry +0x38 writes

The exact selected object base is proven only at consumer sites. At Ghidra
`0x080bf118` / ELF `0x07fbf118`, `SkipAppUpdate` reads
`[selected_entry+0x38]`; it does not write it.

No proven registration or instance initializer is available, so a wider
`+0x38` writer scan would violate the phase boundary.

```text
ENTRY_PLUS_0X38_WRITE_COUNT = 0_IN_PROVEN_PATHS
ENTRY_PLUS_0X38_WRITE_CLASSES = NONE_PROVEN
```
