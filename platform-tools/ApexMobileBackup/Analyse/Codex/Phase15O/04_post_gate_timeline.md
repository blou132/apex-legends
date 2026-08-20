# Post-gate timeline

No post-gate timeline exists because `GameActivity` was never launched.

| Order | Stage | Status |
|---:|---|---|
| 1 | host-mode Android boot | `CONFIRMED` |
| 2 | 60-second SystemUI stability check | `CONFIRMED FAILED` |
| 3 | package/OBB/cache verification | `NOT_REACHED` |
| 4 | network isolation | `NOT_REACHED` |
| 5 | `GameActivity` launch/resume | `NOT_REACHED` |
| 6 | graphics gate/EGL success | `UNKNOWN` |
| 7 | splash/version/update state | `UNKNOWN` |

`POST_RESUME_T0` is undefined for this phase. The first unresolved transition
is `SYSTEMUI_PREFLIGHT_TO_APEX_LAUNCH_BLOCKED`.
