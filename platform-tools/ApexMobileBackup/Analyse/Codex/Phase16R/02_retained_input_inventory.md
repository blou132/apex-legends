# Retained input inventory

| Input | Raw log | Relative timing | Thread role | AppData | Summary |
|---|---|---|---|---|---|
| Phase15U | Available | Available | Available, sanitized | Unavailable | Available |
| Phase16I warm-up | Available | Available | Partial | Three retained snapshots for the phase | Available |
| Phase16I trace | Available | Available | Available, sanitized | Same retained snapshots | Available |
| Phase16M | Available, two bounded launches | Derivable | Puffer caller role only | Unavailable | Available |

Phase15U contains the only retained `version_mgr_imp.cpp` Init and Dolphin
version-action witnesses. Phase16I and Phase16M contain GCloud/Puffer activity
without those witnesses.

```text
PHASE15U_LOG_EVIDENCE = COMPLETE_RETAINED_RAW_RUN
PHASE15U_APPDATA_EVIDENCE = UNAVAILABLE
PHASE16I_WARMUP_LOG_EVIDENCE = COMPLETE_BOUNDED_RAW_RUN
PHASE16I_TRACE_LOG_EVIDENCE = COMPLETE_BOUNDED_RAW_RUN
PHASE16I_APPDATA_SNAPSHOTS = PRELAUNCH_POST_WARMUP_POST_TRACE
PHASE16M_LOG_EVIDENCE = AVAILABLE_NO_ACTIVE_OBSERVATION
```
