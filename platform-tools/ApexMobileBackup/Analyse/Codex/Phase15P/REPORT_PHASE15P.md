# Phase15P - Clean host-GPU retry / first Apex post-graphics stage

Date: 2026-08-20

## Executive result

The mandatory ADB inventory found no physical Android device, WHPX was usable,
and the preserved `ApexPhase9Lab` AVD booted with command-line `-gpu host`,
read-only userdata, and snapshot load/save disabled.

The first targeted SystemUI check was clear at `+1.731 s` after boot completion.
At the `30 s` checkpoint (`+32.169 s` actual), the window dump contained a
visible `Application Not Responding: com.android.systemui` alert. The required
hard stop was applied immediately. No ANR interaction occurred, Apex was not
launched, and package, OBB, cache, network, graphics, screenshot, and client
stage checks were not reached.

The emulator shut down cleanly. No ADB endpoint or target process remains,
network state was never changed, and the preserved AVD hashes and timestamps
are unchanged. This repeats the Phase15O prelaunch SystemUI instability and is
not evidence of an Apex graphics failure.

## Required result

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED WHPX(10.0.26200) INSTALLED_AND_USABLE; EXIT_CODE_0
AVD_BOOT_COMPLETED = YES
GPU_MODE = host

SYSTEMUI_ANR_0S = NO
SYSTEMUI_ANR_30S = YES
SYSTEMUI_ANR_60S = NOT_REACHED_HARD_STOP
SYSTEMUI_ANR_90S = NOT_REACHED_HARD_STOP
SYSTEMUI_ANR_120S = NOT_REACHED_HARD_STOP
SYSTEMUI_PREFLIGHT_STABLE = NO

PACKAGE_STATE_VALID = NOT_REACHED
OBB_STATE_VALID = NOT_REACHED
VALIDATION_CACHE_REUSED = NOT_REACHED
GAMEACTIVITY_RESUMED = NO_CLIENT_NOT_LAUNCHED

GLES_COMPAT_DIALOG_PRESENT = NOT_OBSERVED_CLIENT_NOT_LAUNCHED
GRAPHICS_GATE_PASSED = UNKNOWN_NOT_TESTED

OPENGL_RUNTIME_STAGE = NOT_REACHED
RHI_CONTINUATION_EVIDENCE = NO_NEW_EVIDENCE

SCREEN_STATE_5S = NOT_CAPTURED_POST_RESUME_NOT_REACHED
SCREEN_STATE_15S = NOT_CAPTURED_POST_RESUME_NOT_REACHED
SCREEN_STATE_30S = NOT_CAPTURED_POST_RESUME_NOT_REACHED
SCREEN_STATE_60S = NOT_CAPTURED_POST_RESUME_NOT_REACHED
SCREEN_STATE_120S = NOT_CAPTURED_POST_RESUME_NOT_REACHED

VERSION_BOOTSTRAP_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
UPDATE_ERROR_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
UPDATE_ERROR_CODE = NOT_OBSERVED
I54140715_RUNTIME_WITNESS = NO_NEW_EVIDENCE

UPDATE_UI_OWNER = UNKNOWN
VERSION_BOOTSTRAP_COMPONENT = UNKNOWN
VERSION_REQUEST_FUNCTION = UNKNOWN
VERSION_REQUEST_URL_OR_HOST = UNKNOWN

LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE

FIRST_NEW_POST_GRAPHICS_STAGE = NOT_REACHED
FIRST_UNRESOLVED_TRANSITION = SYSTEMUI_PREFLIGHT_TO_APEX_LAUNCH_BLOCKED

NETWORK_RESTORED = NOT_REQUIRED_NETWORK_UNCHANGED
AVD_SHUTDOWN_CLEAN = YES
APEX_PHASE9LAB_PERSISTENT_CONFIG_UNCHANGED = YES

FINAL_GATE = G REPEATED_HOST_MODE_SYSTEMUI_ANR_STOP
COMMIT = Retry host GLES31 Apex progression Phase15P
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

Phase15P closes this authorized retry at the prelaunch environment gate. It
does not establish any host-mode Apex graphics result. No further host-mode
retry on this AVD is justified without a new technical reason addressing the
repeated SystemUI ANR.
