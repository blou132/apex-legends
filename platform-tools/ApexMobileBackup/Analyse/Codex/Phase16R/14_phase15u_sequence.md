# Phase15U sequence

Only evidenced or one-way-implied elements are retained:

```text
GCLOUD_PUFFER_INIT                 CONFIRMED
-> GCLOUDDOLPHINIMP_INIT           CONFIRMED_INDIRECT_FROM_VERSIONMGR
-> CVERSIONMGR_INIT                CONFIRMED
-> DOLPHIN_NORMAL_CONNECT_ACTION   CONFIRMED
-> OFFLINE_VERSION_RESULT          CONFIRMED
```

Association of provider Init with the exact client `owner+0x1f0` dispatch is
PROBABLE. Interface acquisition, W23, first-extract mode, and the `+0x45`
clear are not proven for this run.

```text
PHASE15U_PROVEN_DOLPHIN_SEQUENCE = PUFFER_INIT -> PROVIDER_DOLPHIN_INIT -> CVERSIONMGR_INIT -> DOLPHIN_VERSION_ACTION
```
