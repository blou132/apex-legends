# Direct BL decode

Ghidra `0x05b314cc` corresponds to ELF `0x05a314cc` and file offset
`0x05a304cc`. The four file bytes form word `0x95264109`.

```text
imm26 = 0x01264109
offset = sign_extend(imm26 << 2) = +0x04990424
target = 0x05a314cc + 0x04990424 = 0x0a3c18f0
```

```text
DIRECT_CALL_RAW_OPCODE = 0x95264109 (little-endian bytes 09 41 26 95)
DIRECT_CALL_MANUAL_TARGET_ELF = 0x0a3c18f0
DIRECT_CALL_MANUAL_TARGET_GHIDRA = 0x0a4c18f0
```
