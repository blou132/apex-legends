# Runtime anchor search

The bounded exact search confirms these runtime metadata anchors:

| Role | Source/component | Class or function | Result |
| --- | --- | --- | --- |
| Puffer manager | `puffer_mgr_imp.cpp`, `puffer_mgr_inter_imp.cpp` | `CPufferMgrImp::Init`, `CPufferMgrImpInter::Init` | found |
| Puffer action | `puffer_init_action.cpp` | `CPufferInitAction::StartAction`, `run`, `MakeSureGetUrlFromServer` | found |
| version manager | `version_mgr_imp.cpp` | `Init` | found |
| version action | `GcloudDolphinVersionAction.cpp` | `dolphin::gcloud_version_action_imp`, `NormalConnectVersionSvr` | found |
| error queue | `action_mgr.cpp` | `OnActionError`, `ProcessActionError` | found |
| client event label | `GemReportHelper` | `ClientEvent`, `UpdateResult` | found |

The following requested identities are absent from the bounded runtime text:
`GCloudPufferImp`, `PufferUpdateService`, `GetUpdateInfo`, `ProcessResult`, and
`ResUpdateCallBack`. Generic unrelated Android callback lines were rejected.

`I54140714` is not present in logcat. Its only exact Phase15U witness remains
the rendered screenshot.

```text
RUNTIME_SOURCE_FILE = puffer_init_action.cpp; version_mgr_imp.cpp; GcloudDolphinVersionAction.cpp; action_mgr.cpp
RUNTIME_CLASS_NAME = CPufferInitAction; CPufferMgrImp; CPufferMgrImpInter; dolphin::gcloud_version_action_imp; GemReportHelper label
RUNTIME_FUNCTION_NAME = MakeSureGetUrlFromServer; NormalConnectVersionSvr; OnActionError; ProcessActionError
RUNTIME_LOG_TAG = GCloud; GPMSDK
RUNTIME_UPDATE_MANAGER_ANCHOR = GCLOUD_DOLPHIN_VERSION_MANAGER
RUNTIME_EVENT_ANCHOR = CLIENTEVENT/UPDATERESULT
```
