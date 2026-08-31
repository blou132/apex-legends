# Historical thread attribution

The retained Phase15U raw log provides a direct same-run mapping:

- The sanitized thread name `Thread-10` appears on a line emitted by the same
  local numeric TID shortly before the version bootstrap.
- The same local TID emits the `version_mgr_imp.cpp` Init witness.
- The same thread also carries GCloud/Puffer caller and engine work.
- `NormalConnectVersionSvr` runs on a separate Dolphin worker.

Numeric TIDs remain local-only.

```text
PHASE15U_VERSIONMGR_THREAD_NAME = Thread-10
PHASE15U_VERSIONMGR_THREAD_ROLE = NATIVE_ENGINE_GCLOUD_DOLPHIN_CALLER
PHASE15U_THREAD_NAME_CONFIDENCE = HIGH
```
