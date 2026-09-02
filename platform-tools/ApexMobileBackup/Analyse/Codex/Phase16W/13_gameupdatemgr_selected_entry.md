# GameUpdateMgr selected entry

`SkipAppUpdate` (`FUN_080bf050`) obtains the selected value through a bounded
lookup:

1. Load `[argument0+0x168]` and add `0xe8`.
2. Use the collection at that base plus `0x10`.
3. Resolve an index with `FUN_04214458` and a global selector key.
4. Address a 24-byte element and load its value pointer from element `+0x08`.
5. Validate the selected value's class relation before using it as the entry.

```text
GAMEUPDATEMGR_SELECTED_ENTRY_SOURCE = VALUE_PLUS_0X08_FROM_SELECTED_24_BYTE_COLLECTION_ELEMENT_AT_[ARG0_PLUS_0X168]_PLUS_0XE8_PLUS_0X10
GAMEUPDATEMGR_OWNER_FIELD_OFFSET = 0x38
```
