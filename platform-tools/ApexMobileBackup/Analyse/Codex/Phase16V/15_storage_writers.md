# Storage writers

Writer analysis was eligible only after `DolphinCallback+0x08` was promoted as
the exact persistent location. The bounded prior initializer evidence was then
revalidated for this field only.

Ghidra `0x0a2a4044` (ELF `0x0a1a4044`) executes:

```asm
stp x8, xzr, [x0]
```

This stores the callback table at `+0x00` and null at `+0x08`. No non-null
assignment with the same callback-base provenance is proven. No displacement-
only or global writer scan was performed.

```text
OWNER_STORAGE_WRITER_SEARCH_ELIGIBLE = YES
OWNER_STORAGE_WRITE_COUNT = 1
OWNER_STORAGE_WRITE_CLASSES = NULL_INIT
```
