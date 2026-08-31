# CreateDolphin relocation

Direct parsing of `.rela.plt`, `.dynsym`, and `.dynstr` resolves:

```text
CREATEDOLPHIN_DYNSYM_INDEX = 375
CREATEDOLPHIN_RELA_PLT_INDEX = 529 (zero-based)
CREATEDOLPHIN_RELA_PLT_FILE_OFFSET = 0x34b0
CREATEDOLPHIN_GOT_CELL = 0x0b37e8c8
CREATEDOLPHIN_RELOCATION_TYPE = R_AARCH64_JUMP_SLOT (1026)
```

The exact neighboring entries are index 528 `IGCloud::GetInstance`, index 530
`DolphinHelper::GetCurApkPath`, and index 531 `ReleaseDolphin`.
