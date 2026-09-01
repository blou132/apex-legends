# Thread role

The retained Phase15U mapping associates the version-manager witness with the
sanitized thread name `Thread-10`, acting as the native engine GCloud/Dolphin
caller. The same caller role also carries GCloud/Puffer work. The later
`NormalConnectVersionSvr` action uses a separate Dolphin worker.

Phase16M independently correlates a same-named native GCloud/Puffer caller but
contains no active Dolphin observation. No numeric TID is published.

```text
DOLPHIN_VERSION_PATH_THREAD = Thread-10 / NATIVE_ENGINE_GCLOUD_DOLPHIN_CALLER
PUFFER_PATH_THREAD = NATIVE_ENGINE_GCLOUD_PUFFER_CALLER
THREAD_ROLE_RELATION = SHARED_NATIVE_CALLER_ROLE; DOLPHIN_NETWORK_ACTION_SEPARATE_WORKER
```
