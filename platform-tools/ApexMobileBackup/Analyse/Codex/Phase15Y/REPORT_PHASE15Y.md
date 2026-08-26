# Phase15Y - existing Phase15U Puffer evidence mining

Date: 2026-08-26

## Executive result

Phase15Y used only the existing gitignored Phase15U evidence. No runtime,
device, ADB, emulator, network, or client operation occurred.

The bounded log resolves the actual Puffer failure branch. After two offline
connection waits, `CPufferInitAction::MakeSureGetUrlFromServer` reports
`connect server timeout`, and `CPufferInitAction::run` returns decimal
`70254639`, exactly `0x0430002f` from the Phase15V branch table.

The same window exposes a second, earlier update path that was not separated in
the Phase15U report. GCloud Dolphin's `version_mgr_imp.cpp` starts a named
version manager, `NormalConnectVersionSvr` reports the offline failure, and
`GemReportHelper` emits `ClientEvent` / `UpdateResult` with decimal
`154140714` (`0x0930002a`) and result `-1`. That event precedes the `+120 s`
capture of `2/17` and `I54140714`; Puffer's final `0x0430002f` result follows
the capture by about 39 seconds.

An exact read-only Ghidra follow-up anchors `ProcessActionError` at
`libgcloud.so` `0x005be71c` and finds `VFS_Puffer_OnUpdateResult` at
`0x005dfe58`. The latter reaches a QTCVFS `SetInitReturn` callback boundary,
but it does not identify the application object supplied to
`GCloudPufferImp::Init` or resolve the Phase15X external slot `+0x10`.

The strongest result is therefore a named client-side update manager anchor,
not an application callback identity. `I54140714` remains screenshot-only;
its numeric correlation with the Dolphin result is strong but construction and
UI ownership are not proven.

## Required result

```text
PHASE15U_RAW_EVIDENCE_FOUND = YES

RUNTIME_PUFFER_INTERNAL_RESULT = 70254639 (0x0430002f)
PHASE15U_PUFFER_FAILURE_BRANCH = CONNECT_SERVER_TIMEOUT
PHASE15U_PUFFER_INTERNAL_CODE = 70254639 (0x0430002f)

RUNTIME_SOURCE_FILE = puffer_init_action.cpp; version_mgr_imp.cpp; GcloudDolphinVersionAction.cpp; action_mgr.cpp
RUNTIME_CLASS_NAME = CPufferInitAction; CPufferMgrImp; CPufferMgrImpInter; dolphin::gcloud_version_action_imp; GemReportHelper label
RUNTIME_FUNCTION_NAME = MakeSureGetUrlFromServer; NormalConnectVersionSvr; OnActionError; ProcessActionError

RUNTIME_CLIENT_CALLBACK_ANCHOR = NONE
RUNTIME_UPDATE_MANAGER_ANCHOR = GCLOUD_DOLPHIN_VERSION_MANAGER
RUNTIME_EVENT_ANCHOR = CLIENTEVENT/UPDATERESULT

I54140714_LOGCAT_ANCHOR = NOT_FOUND

NEW_RUNTIME_ANCHOR = GCLOUD_DOLPHIN_VERSION_MANAGER_AND_UPDATERESULT_EVENT
ANCHOR_BINARY = libgcloud.so
ANCHOR_GHIDRA_ADDRESS = 0x005be71c; 0x005dfe58
ANCHOR_FUNCTION = FUN_005be71c (ProcessActionError); FUN_005dfe58 (VFS_Puffer_OnUpdateResult)

CLIENT_CALLBACK_CLASS = UNKNOWN
CLIENT_CALLBACK_SLOT_10_TARGET = UNKNOWN

CLIENT_UPDATE_MANAGER = dolphin::gcloud_version_action_imp / GCloud Dolphin version manager
PUFFER_TO_UPDATE_MANAGER_DIRECT_EDGE = NO

I54140714_CONSTRUCTION_CONFIRMED = NO

EXISTING_RUNTIME_LOG_AXIS_EXHAUSTED = YES
NEXT_METHODOLOGY_CANDIDATE = CLIENT_CALLBACK_REGISTRATION_RUNTIME_TRACE

CURRENT_BLOCKER = NO_CLIENT_CALLBACK_IDENTITY_IN_EXISTING_RUNTIME_EVIDENCE
NEXT_RUNTIME_LAUNCH_REQUIRED = NO

FINAL_GATE = C NAMED_CLIENT_UPDATE_MANAGER_ANCHORED
COMMIT = Mine existing Puffer runtime evidence Phase15Y
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Privacy and service boundary

Published evidence contains no device serial, raw PID/TID, account value,
token, network identifier, or private host path. TDM remains telemetry,
GCloudCore/TGPA remain remote config, and Puffer/Dolphin remain version/update
components. Login, server-list, and game-server ownership remain unknown.
