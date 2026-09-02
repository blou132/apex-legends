# CheckUpdate this provenance

Bounded caller-side slicing recovers all four `X0` sources:

| Callsite ELF | Proven `X0` source |
|---|---|
| `0x05a87b78` | `FUN_05b85d18` argument 0, preserved in `X19` |
| `0x05a897ec` | `FUN_05b89618` argument 0, preserved in `X19` |
| `0x05c38300` | `FUN_05d38254` argument 0, preserved through the stack |
| `0x07fbf14c` | `[selected GameUpdateMgr entry + 0x38]`, preserved in `X19` |

The fourth path null-tests the selected entry pointer, then checks owner
`+0x1f0` and status byte `+0x44` before invoking CheckUpdate.

```text
CHECKUPDATE_THIS_SOURCES = STARTUPDATE_ARG0; ONSRCUPDATEFINISHED_ARG0; ONUPDATESUCCESS_ARG0; SELECTED_GAMEUPDATEMGR_ENTRY_PLUS_0x38
```
