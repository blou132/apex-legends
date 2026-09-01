# Neighbor methods

The bounded singleton review decompiled exactly eight cells before Shutdown
and two after it.

| Relative cell | Function | Bounded role |
| ---: | --- | --- |
| -8 | `FUN_0a001dd8` | constant zero stub |
| -7 | `FUN_0a001de0` | return-only stub |
| -6 | external thunk at `0x083bcb3c` | unresolved external branch thunk |
| -5 | `FUN_0a001f1c` | return-only stub |
| -4 | `FUN_0a001de4` | return-only stub |
| -3 | `FUN_0a001de8` | constant minus-one stub |
| -2 | `FUN_0a001df0` | return-only stub |
| -1 | `FUN_0a001df4` | constant minus-one stub |
| 0 | `FUN_04812c6c` | known DolphinUpdater::Shutdown |
| +1 | `FUN_0a2a4024` | constant one stub |
| +2 | `FUN_0a2a4034` | return-only stub |

No neighbor contains an exact Dolphin identifier or bounded semantic evidence
for Init, Start, Setup, Create, CheckUpdate, Update, FirstExtract, or callback
setup.

`DOLPHINUPDATER_NEIGHBOR_METHODS = GENERIC_STUBS_ONLY; NO_DOLPHIN_LIFECYCLE_IDENTITY`
