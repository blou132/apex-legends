# Phase15J - official client logs and Apex dialog

Date: 2026-08-20

## Executive result

No physical device was present. WHPX was usable and the unchanged AVD booted
without wipe, reinstall, or snapshots. Five official SDK logs were readable
before launch, but their only `Login` occurrences were generic MSDK provider
configuration, not an Apex client stage. This justified one bounded offline
run.

The valid OBB cache was reused, `GameActivity` resumed, and Apex remained alive
through +180 seconds. Three new official files appeared by +30 seconds: a
GCloudCore log, an empty GCloud log, and an MSDK XLog file. They contain SDK
initialization/configuration and offline request activity only. Neither those
files nor filtered logcat add a Lua, ClientLaunch, EventSystem, LoginMgr,
server-list, splash-completion, or splash-dismiss witness.

The Apex dialog reappeared as a third `GameActivity` Android window between +15
and +30 seconds and persisted through +180 seconds. Its title and message remain
unresolved because the permitted UI command returned no hierarchy XML through
the host capture while the already-present SystemUI ANR/immersive overlay
remained foreground. No click was performed, and no direct edge makes the Apex
dialog or a network retry the first blocking dependency.

## Required results

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES

PUBLIC_SDK_LOG_DIRECTORY_PRESENT = YES
PREEXISTING_LOG_FILES_FOUND = 5
NEW_RUNTIME_RUN_REQUIRED = YES

VALIDATION_CACHE_REUSED = YES
GAMEACTIVITY_RESUMED = YES

APEX_OK_DIALOG_PRESENT = YES
APEX_OK_DIALOG_MESSAGE_RESOLVED = NO
APEX_OK_DIALOG_ROLE = UNKNOWN

OFFICIAL_FILE_LOGS_READABLE = YES
NEW_OFFICIAL_LOG_MESSAGES = YES_SDK_ONLY

SPLASH_VIDEO_COMPLETION_RUNTIME = NO_NEW_EVIDENCE
SPLASH_DISMISS_RUNTIME = NO_NEW_EVIDENCE
NATIVE_SPLASH_DISMISS_TRIGGER = UNKNOWN

LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENTSYSTEM_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
SERVER_LIST_EVENT_0X138_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE

FIRST_BLOCKING_DEPENDENCY = UNKNOWN

NETWORK_RESTORED = YES
AVD_SHUTDOWN_CLEAN = YES

FINAL_GATE = D OFFICIAL_LOGS_READABLE_SDK_ONLY_NO_CLIENT_STAGE
COMMIT = Read official client logs Phase15J
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence limit

Phase15J proves that official public SDK logs are readable and written by this
run. It does not prove that UE4 client stages are absent, resolve the Apex dialog
message, identify the native splash-dismiss trigger, or establish TDM/GCloud as
a bootstrap dependency.
