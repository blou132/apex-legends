# Target TID resolution

Ptrace hardware state is per-thread, so the process leader was rejected.

The selected strategy was to wait for the fresh Apex process, require the exact
executable `libgcloud.so` mapping, then select the live thread whose comm name
was exactly `Thread-10`. Evidence was:

1. historical runtime logs placed `CVersionMgrImp::Init` on the native GCloud
   caller thread;
2. the current warm-up explicitly associated the GCloud/Puffer caller TID with
   Android thread name `Thread-10`;
3. transport actions used separate worker TIDs and were not selected.

This resolved one thread without spraying breakpoints. Because the active
window later timed out and the Dolphin path was absent from logs, final
confidence remains medium rather than conclusive.

```text
TARGET_TID_STRATEGY = EXACT_THREAD_NAME_Thread-10_AFTER_LIBGCLOUD_MAP
TARGET_TID_RESOLVED = YES
TARGET_TID_CONFIDENCE = MEDIUM
```
