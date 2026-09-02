# SkipAppUpdate lookup

`SkipAppUpdate` is `FUN_080bf050`. At Ghidra `0x080bf0b4` / ELF
`0x07fbf0b4`, it calls `FUN_04214458` with:

- collection: `[[argument0+0x168]+0xe8]+0x10`;
- key: the value loaded from the lazy class cell for
  `/Script/PureClient.GameUpdateMgr`.

The returned signed index is checked against `-1`. A valid index selects a
`0x18`-byte element and loads the object pointer from element `+0x08`.

```text
SELECTED_LOOKUP_CALLSITE = GHIDRA_0x080bf0b4_ELF_0x07fbf0b4
SELECTED_LOOKUP_COLLECTION_SOURCE = [[ARG0_PLUS_0X168]_PLUS_0XE8]_PLUS_0X10
SELECTED_LOOKUP_KEY_SOURCE = LAZY_UCLASS_CELL_FOR_/Script/PureClient.GameUpdateMgr
```
