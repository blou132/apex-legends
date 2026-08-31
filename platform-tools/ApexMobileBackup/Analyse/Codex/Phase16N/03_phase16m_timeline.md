# Phase16M relative timeline

`T0` is the first retained Apex process log, not a measured process-creation
instant.

| Relative point | Sanitized event |
|---|---|
| T0 | First retained process log |
| T0 + 3.302 s | Selected instance first emits a graphics-driver event |
| T0 + 4.063 s | Selected instance emits GCloudCore `CreateEvent` |
| T0 + 9.550 s | Same instance emits the Puffer caller witness |
| Before task snapshot | `libUE4` and GCloud modules are mapped |
| T0 + 358.8 s | Task snapshot contains five `Thread-10` instances |
| T0 + 359.7 s | Read-only pre-active attach attempt returns `EPERM` |
| T0 + 447.7 s | Non-zero tracer state is retained for the target |
| T0 + 478.0 s | Existing tracer identity is confirmed as GameProtector3 |
| Final watcher interval | Puffer correlation and existing ownership coexist |
| Cleanup | Apex is force-stopped; no active observation was consumed |

Module mapping snapshots do not provide a defensible earlier relative mapping
instant, so the timeline does not invent one.
