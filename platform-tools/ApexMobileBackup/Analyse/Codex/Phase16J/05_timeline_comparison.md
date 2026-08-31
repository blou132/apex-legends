# Timeline comparison

`NOT_OBSERVED` means absent from the available bounded instrumentation; it is
not automatically promoted to proof of non-execution.

| Event | Phase15U | Phase16I warm-up | Phase16I trace launch |
|---|---|---|---|
| `libgcloud.so` | OBSERVED | OBSERVED | OBSERVED |
| GCloud initialization | OBSERVED | OBSERVED | UNKNOWN |
| Puffer initialization | OBSERVED | OBSERVED | NOT_OBSERVED |
| Puffer retry/failure | OBSERVED | OBSERVED | NOT_OBSERVED |
| Version-manager Init | OBSERVED | NOT_OBSERVED | NOT_OBSERVED |
| Dolphin version action | OBSERVED | NOT_OBSERVED | NOT_OBSERVED |
| Update UI/progress | OBSERVED | NOT_OBSERVED | NOT_OBSERVED |
| Offline terminal/retry state | OBSERVED | OBSERVED | NOT_OBSERVED |

Phase15U and Phase16I reached different update/bootstrap branches. Phase16I
proved that Puffer can initialize and retry without entering the Dolphin
version-manager branch.
