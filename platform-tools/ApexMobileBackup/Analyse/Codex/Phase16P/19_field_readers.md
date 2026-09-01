# Dolphin field readers

Four exact read sites are proven in the bounded owner lifecycle.

| Ghidra site | ELF site | Function | Use |
|---|---|---|---|
| `0x04812c88` | `0x04712c88` | `DolphinUpdater::Shutdown` | null test, optional slot `+0x40` tail dispatch |
| `0x04812ec4` | `0x04712ec4` | `DolphinUpdater::Shutdown` | cleanup slot `+0x18`, then clear |
| `0x05b2f094` | `0x05a2f094` | `DolphinUpdater::CheckUpdate` | load interface and dispatch slot `+0x10` |
| `0x05b2f0b4` | `0x05a2f0b4` | `DolphinUpdater::CheckUpdate` | success follow-up slot `+0x28` |

```text
DOLPHIN_OWNER_FIELD_READER_COUNT = 4_EXACT_READ_SITES
```
