# GameUpdateMgr owner field

At Ghidra `0x080bf118` / ELF `0x07fbf118`, `SkipAppUpdate` loads
`[selected_entry+0x38]`, null-tests it, then validates owner `+0x1f0` and
status `+0x44` before calling CheckUpdate.

The selected-entry path contains no write to that exact field. Without an
independently proven entry constructor, expanding to other `+0x38` stores
would be a forbidden displacement scan.

```text
GAMEUPDATEMGR_OWNER_FIELD_WRITE_COUNT = 0
GAMEUPDATEMGR_OWNER_FIELD_WRITE_CLASSES = NONE_PROVEN
```
