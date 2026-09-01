# Owner initial fields

Because no owner initializer is proven, initial writes and initial values for
`+0x1f0` and `+0x1f8` cannot be established.

The minimal evidence-backed layout is:

| Field | Bounded result |
|---|---|
| callback | placement unknown; callback `+0x08` holds an owner pointer |
| owner `+0x45` | persistent state flag; initial value unknown |
| owner `+0x1f0` | persistent Dolphin interface; initial value unknown |
| owner `+0x1f8` | adjacent related-interface state probable; initial value unknown |

```text
OWNER_PLUS_0X1F0_INITIAL_WRITE = UNKNOWN
OWNER_PLUS_0X1F0_INITIAL_VALUE = UNKNOWN
OWNER_PLUS_0X1F8_INITIAL_WRITE = UNKNOWN
OWNER_PLUS_0X1F8_INITIAL_VALUE = UNKNOWN
DOLPHINUPDATER_CONSTRUCTION_LAYOUT = CALLBACK_PLACEMENT_UNKNOWN; +0x45_STATE_FLAG; +0x1f0_DOLPHIN_INTERFACE; +0x1f8_RELATED_INTERFACE_PROBABLE
```
