# Executing thread resolution

The Phase15U version-manager Init witness and a nearby `Thread-10` naming line
share the same local TID in the same run. That is the direct runtime witness
required for high-confidence attribution. The later Dolphin transport action
uses a different worker and does not change the Init executor.

```text
CVERSIONMGR_EXECUTING_THREAD_NAME = Thread-10
CVERSIONMGR_EXECUTING_THREAD_ROLE = NATIVE_ENGINE_GCLOUD_DOLPHIN_CALLER
CVERSIONMGR_EXECUTING_THREAD_CONFIDENCE = HIGH
```
