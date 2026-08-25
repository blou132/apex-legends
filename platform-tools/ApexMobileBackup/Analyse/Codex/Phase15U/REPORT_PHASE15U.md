# Phase15U - PRA-LX1 bounded offline Apex runtime

Date: 2026-08-25

## Executive result

Exactly one offline Apex launch was performed on the authorized Huawei
PRA-LX1. The client started `GameActivity`, traversed the short
`DownloaderActivity` expansion-file transition, resumed native/UE4 execution,
created working Mali EGL surfaces, loaded the launch level, and rendered the
update module. This confirms that the physical GLES 3.2 device clears the
earlier emulator graphics compatibility gate.

At the `+120 s` capture, the client displayed update progress `2/17` and an
offline network update failure with exact code `I54140714`. The historical code
`I54140715` did not appear. One UIAutomator dump exposed no client text because
the modal is UE4-rendered. No UI action, account, request response, redirection,
proxy, backend, debugger, or profiler was used.

Runtime logs separately confirm TDM telemetry, GCloudCore/TGPA remote config,
and GCloud Puffer update attempts. Puffer enters
`CPufferInitAction::MakeSureGetUrlFromServer` and fails DNS/connectivity while
offline. No direct edge assigns the rendered error code to Puffer, so exact UI
ownership remains unknown. A targeted post-stop static exact-string search did
not locate either `I54140714` or `I54140715`.

Apex was force-stopped at the authorized 180-second hard limit, the network was
restored, and package/OBB integrity was reconfirmed. Runtime storage use was
approximately 15.58 MiB and no storage guard triggered.

## Required result

```text
PHONE_MODEL = PRA-LX1
ANDROID_VERSION = 8.0.0 / API 26
PRIMARY_ABI = arm64-v8a

FREE_SPACE_PRELAUNCH = 2122084 KiB (2.024 GiB)
FREE_SPACE_AT_LAUNCH = 2164200 KiB (2.064 GiB)
FREE_SPACE_POSTRUN = 2148248 KiB (2.049 GiB)
RUNTIME_STORAGE_DELTA = -15952 KiB free (15.58 MiB consumed)

APEX_PACKAGE_INTACT = YES
APEX_OBBS_INTACT = YES

NETWORK_ISOLATED = YES
NETWORK_RESTORED = YES

APEX_LAUNCH_COUNT = 1
GAMEACTIVITY_STARTED = YES
DOWNLOADERACTIVITY_STARTED = YES
GAMEACTIVITY_RESUMED = YES

GLES_COMPAT_DIALOG_PRESENT = NO
PHYSICAL_DEVICE_GRAPHICS_GATE_PASSED = CONFIRMED

VERSION_BOOTSTRAP_RUNTIME_EVIDENCE = CONFIRMED
UPDATE_ERROR_RUNTIME_EVIDENCE = CONFIRMED
UPDATE_ERROR_CODE = I54140714
I54140715_RUNTIME_WITNESS = NO_NEW_EVIDENCE

LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE

FIRST_NEW_RUNTIME_STAGE = UPDATE_MODULE_INITIALIZATION_2_OF_17_THEN_UPDATE_ERROR
FIRST_UNRESOLVED_TRANSITION = EXACT_UPDATE_UI_OWNER_AND_ERROR_CODE_TO_SERVICE_MAPPING

STORAGE_RUNTIME_GUARD_TRIGGERED = NO

APEX_PACKAGE_INTACT_POSTRUN = YES
APEX_OBBS_INTACT_POSTRUN = YES

FINAL_GATE = A VERSION_BOOTSTRAP_AND_UPDATE_ERROR_CONFIRMED
COMMIT = Observe PRA-LX1 Apex version bootstrap Phase15U
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

The published result contains no ADB serial, IMEI, Android ID, MAC, account
data, token, raw process identifier, randomized package path, or private host
path. Raw evidence remains local-only and gitignored.
