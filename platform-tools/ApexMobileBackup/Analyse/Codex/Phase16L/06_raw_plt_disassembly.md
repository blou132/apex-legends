# Raw PLT decode

The exact four instruction words at ELF `0x0a3c18f0` decode as:

```text
b0007df0  adrp x16, 0x0b37e000
f9446611  ldr  x17, [x16, #0x8c8]
91232210  add  x16, x16, #0x8c8
d61f0220  br   x17
```

Both the load and add derive GOT cell `0x0b37e8c8`, exactly matching the
`CreateDolphin` relocation. Ghidra `0x0a4c18fc` is the final `br x17` in this
stub, not a separate thunk or factory consumer.

```text
CREATEDOLPHIN_PLT_STUB_DECODE_VALID = YES
PLT_TO_GOT_MATCH = YES
COMPUTED_CALL_SITE_CLASS = FINAL_BR_IN_STANDARD_AARCH64_PLT_STUB
COMPUTED_CALL_REAL_TARGET_SYMBOL = CreateDolphin
```
