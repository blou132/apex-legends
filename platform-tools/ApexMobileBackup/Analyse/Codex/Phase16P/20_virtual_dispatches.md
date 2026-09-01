# Virtual dispatches

All listed slots originate from exact reads of `DolphinUpdater+0x1f0` in the
bounded owner methods.

| Slot | Site | Classification |
|---:|---|---|
| `+0x10` | Ghidra `0x05b2f0ac` | Init ABI match |
| `+0x18` | Ghidra `0x04812ed4` | shutdown cleanup |
| `+0x28` | Ghidra `0x05b2f0c0` | post-Init success follow-up |
| `+0x40` | Ghidra `0x04812cd8` | conditional tail dispatch |

```text
DOLPHIN_INTERFACE_VIRTUAL_SLOTS_USED = +0x10; +0x18; +0x28; +0x40
```
