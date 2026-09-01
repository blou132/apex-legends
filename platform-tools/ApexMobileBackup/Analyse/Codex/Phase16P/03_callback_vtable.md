# Callback table

The success function has one exact pointer cell at Ghidra `0x0af551a8`. The
contiguous table begins at Ghidra `0x0af55170` (ELF `0x0ae55170`) and contains
the neighboring Dolphin callbacks.

| Slot | Target | Identified role |
|---|---|---|
| `+0x00` | `FUN_0a2a403c` | table/owner initializer |
| `+0x10` | `FUN_05b85c9c` | `OnDolphinVersionInfo` |
| `+0x20` | `FUN_0623f800` | `OnDolphinError` |
| `+0x28` | `FUN_05d381dc` | `OnDolphinSuccess` |
| `+0x30` | `FUN_080f8754` | `OnDolphinNoticeInstallApk` |
| `+0x38` | `FUN_080f87d0` | `OnDolphinFirstExtractSuccess` |
| `+0x40` | `FUN_080f883c` | `OnDolphinServerCfgInfo` |

The exact table address is constructed once, by `FUN_0a2a403c`. The table
shape and exact callback identifiers support high confidence, while its exact
C++ ABI label remains conservative.

```text
DOLPHIN_CALLBACK_VTABLE = 0x0af55170
DOLPHIN_CALLBACK_VTABLE_CONFIDENCE = HIGH
```
