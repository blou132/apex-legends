# Minimal owner layout

Only fields directly supported by the bounded Dolphin lifecycle are retained.

| Object | Offset | Role | Confidence |
|---|---:|---|---|
| `DolphinCallback` | `+0x00` | callback function-table pointer | Confirmed |
| `DolphinCallback` | `+0x08` | `DolphinUpdater` owner pointer | Probable |
| `DolphinUpdater` | `+0x1f0` | persistent Dolphin interface pointer | Confirmed as lifecycle field |
| `DolphinUpdater` | `+0x1f8` | adjacent interface/config pointer passed to Init and separately cleaned | Confirmed field, identity unknown |

```text
DOLPHIN_OWNER_MINIMAL_LAYOUT = CALLBACK(+0x00_TABLE,+0x08_OWNER); DOLPHINUPDATER(+0x1f0_DOLPHIN,+0x1f8_ADJACENT_UNKNOWN)
```
