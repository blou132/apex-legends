# Bounded Puffer and update timeline

Relative times use the exact Phase15U `GameActivity` start as `t0`.

| Relative time | Component | Exact observation |
| ---: | --- | --- |
| about `+109.1 s` | Android activity | `GameActivity` resumes |
| about `+116.6 s` | Puffer | `CPufferMgrImp::Init`, `CPufferMgrImpInter::Init`, then `CPufferInitAction::StartAction` |
| about `+116.6 s` | Puffer | `MakeSureGetUrlFromServer` starts URL retrieval and RPC initialization |
| about `+116.7 s` | Puffer transport | Puffer host DNS resolution fails; connection wait begins |
| about `+118.2 s` | Dolphin version manager | `version_mgr_imp.cpp::Init` starts the GCloud version path |
| about `+118.3 s` | Dolphin version action | `NormalConnectVersionSvr` reports that the network is not reachable; `OnActionError` is queued |
| about `+118.3 s` | GPMSDK event | `GemReportHelper` emits `ClientEvent`, `UpdateResult`, code `154140714`, result `-1` |
| about `+118.4 s` | GCloud action manager | `ProcessActionError` records error stage `69`, error code `154140714`, and cancels the action |
| `+120 s` | rendered UI | first non-black capture shows update initialization `2/17` and `I54140714` |
| about `+137.7 s` | Puffer | first `connect server timeout`; another RPC attempt starts |
| about `+159.0 s` | Puffer | second timeout; `CPufferInitAction::run` returns `70254639` |
| about `+159.0 s` | Puffer manager | a new Puffer initialization begins on a different application thread |

The Dolphin `UpdateResult` event precedes the UI capture. The final Puffer
result follows the capture by about 39 seconds. These are two concurrent GCloud
update paths; temporal proximity is not a direct ownership edge.

```text
PUFFER_CALLBACK_THREAD_RELATION = DIFFERENT_UNNAMED_THREADS; NO_CLIENT_CONSUMER_IDENTIFIED
PUFFER_TO_UI_TEMPORAL_ORDER = DOLPHIN_UPDATE_RESULT -> UI_CAPTURE -> PUFFER_FINAL_RESULT
```
