# CVersionMgr trigger gates

Directly evidenced gates in the bounded static chain:

| Gate | Evidence | Confidence |
|---|---|---|
| Dolphin Init inputs are valid | `FUN_005458a0` rejects null callback/config/path inputs | High |
| Manager allocation succeeds | `FUN_005458a0` checks the `FUN_00506b14` result | High |
| Update type is supported | `FUN_005458a0` validates and switches on the update type | High |
| Version configuration is constructed | `FUN_005458a0` builds structured settings before slot `+0x10` | High |
| External callback exists | `FUN_00576180` requires a non-null wrapper and non-null dereferenced callback | High |
| Internal action manager initialization succeeds | `FUN_00576180` checks two initialization stages | High |
| Strategy construction succeeds | Init stores an instance-owned strategy before success | High |

No singleton, process-global once flag, AppData key, or Puffer completion
predicate was directly found in this target neighborhood. The unresolved gate
is above the Dolphin vtable: what makes the client choose and call
`GCloudDolphinImp::Init` in a given bootstrap run.

```text
CVERSIONMGR_INIT_STATIC_GATES = VALID_DOLPHIN_INPUTS; MANAGER_ALLOCATION; SUPPORTED_UPDATE_TYPE; CONFIG_BUILD; EXTERNAL_CALLBACK; INTERNAL_INIT; STRATEGY_CREATE
```
