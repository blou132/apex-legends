# Provider Init ABI

The primary `GCloudDolphinImp` table starts at `0x00979620`. Cell
`0x00979630`, slot `+0x10`, contains `FUN_005458a0`.

Ghidra defines the function body as `0x005458a0..0x00547c4f` inclusive. Entry
tracking gives this bounded register-use map:

| Register | First bounded use |
|---|---|
| `X0` | provider `this`, preserved in `D12`; later used as base for `+0x10` and `+0x18` |
| `X1` | copied to `X26`, null-tested, then dereferenced as configuration |
| `X2` | copied to `X19`, null-tested, then used as path/input state |
| `X3` | preserved in `D8`; auxiliary Init input |
| `X4` | copied to `X27`; optional Init input |
| `X5` | null-tested, then stored at `[X0+0x10]` |
| `X6` | no entry value use before overwrite |
| `X7` | no entry value use before overwrite |

The update type is read from the configuration supplied in `X1`, specifically
its `+0x04` field; it is not a separate entry register.

```text
GCLOUDDOLPHIN_INIT_FUNCTION = FUN_005458a0
GCLOUDDOLPHIN_INIT_FUNCTION_RANGE = GHIDRA_0x005458a0..0x00547c4f_INCLUSIVE
GCLOUDDOLPHIN_INIT_SLOT_VALID = YES
GCLOUDDOLPHIN_CONFIG_ARGUMENT = X1
GCLOUDDOLPHIN_PATH_ARGUMENT = X2
GCLOUDDOLPHIN_UPDATE_TYPE_ARGUMENT = X1_CONFIG_PLUS_0x04
```
