# First-run state assessment

Five candidate categories were retained as semantic leads: GCloud config,
Unreal splash state, measurement first-open state, Singular install state,
and TRI first-init state.

The strongest structural lead is the GCloud config pair, but Phase15U AppData
was not retained and the static Init chain does not directly read those files.
The `SPLASH_VIDEO_START` key appeared only after the Phase16I trace launch, so
it cannot explain why the earlier warm-up lacked the target.

```text
FIRST_RUN_PERSISTENT_STATE_CANDIDATES = 5
PHASE15U_APPDATA_COMPARISON_AVAILABLE = NO
CVERSIONMGR_INIT_APPDATA_DEPENDENCE = UNKNOWN
```
