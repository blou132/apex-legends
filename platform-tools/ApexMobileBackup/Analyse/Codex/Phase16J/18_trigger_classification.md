# Trigger classification

`CVersionMgrImp::Init` is reached during `GCloudDolphinImp::Init`, after
Dolphin creates the manager and constructs its update configuration. It is
not a direct Puffer retry callback and is distinct from CheckAppUpdate.

The historical/current difference is upstream: Phase15U entered the Dolphin
version bootstrap, while Phase16I entered Puffer activity without invoking
the observed Dolphin Init branch. The exact client-side predicate selecting
that branch is not resolved by the bounded evidence.

```text
CVERSIONMGR_INIT_TRIGGER_CLASS = GCLIENT/GPM_SDK_INITIALIZATION
CVERSIONMGR_INIT_APPDATA_DEPENDENCE = UNKNOWN
PHASE16I_WARMUP_LIKELY_CONSUMED_INIT = NO
```

H5 is supported. H1, H3, H4, and H6 are not supported. H2 remains possible
but unproven because Phase15U AppData is unavailable and no direct AppData
gate appears in the static target neighborhood.
