# Evidence inventory

| Category | Availability | Ordering | Source |
|---|---|---|---|
| Historical raw runtime log | Available | One Phase15U run | Phase15U |
| Historical AppData snapshot | Missing | Not retained | Phase15U |
| Warm-up raw runtime log | Available | Before active trace | Phase16I |
| Trace-launch raw runtime log | Available | After warm-up | Phase16I |
| Thread and timing material | Available | Warm-up and trace launch | Phase16I |
| AppData snapshot 0 | Available | Prelaunch | Phase16I |
| AppData snapshot 1 | Available | Post-warm-up | Phase16I |
| AppData snapshot 2 | Available | Post-trace | Phase16I |
| Exact local `libgcloud.so` and Ghidra project | Available | Static baseline | Phase10/Phase16I |

Raw artifacts remain local-only under `Analyse/LocalInputs`.

```text
PHASE15U_RAW_EVIDENCE_AVAILABLE = YES
PHASE16I_RAW_EVIDENCE_AVAILABLE = YES
APPDATA_SNAPSHOT_SET_COMPLETE = YES
```
