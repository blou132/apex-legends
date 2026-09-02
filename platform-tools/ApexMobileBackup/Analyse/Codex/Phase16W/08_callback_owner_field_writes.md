# Callback owner field writes

The exact class-level callback initializer `FUN_0a2a403c` writes the callback
table and null owner together. At Ghidra `0x0a2a4044` / ELF `0x0a1a4044`:

```asm
stp x8, xzr, [x0]
```

This is one exact `callback+0x08` `NULL_INIT`. No non-null write is present in
functions with proven callback-base provenance. No displacement-only search
was performed.

```text
CALLBACK_OWNER_FIELD_TARGET_VALID = YES_FOR_EXACT_CALLBACK_CANDIDATE
CALLBACK_PLUS_0X08_WRITE_COUNT = 1
CALLBACK_PLUS_0X08_WRITE_CLASSES = NULL_INIT
```
