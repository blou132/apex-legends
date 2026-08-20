# Phase15O - Host GLES31 Apex progression observation

Date: 2026-08-20

## Executive result

The mandatory ADB preflight found no physical Android device, and WHPX was
usable. The preserved `ApexPhase9Lab` AVD cold-booted successfully with the
command-line-only `-gpu host` override, read-only userdata, and snapshot
load/save disabled.

After the required 60-second prelaunch wait, a targeted window dump showed a
visible `Application Not Responding: com.android.systemui` system alert. The
specified stop gate was applied immediately: there was no interaction with the
ANR, no package/OBB/cache or network phase, and no Apex launch. Consequently
Phase15O does not answer whether host GLES31 clears the client graphics dialog
or reaches version/update bootstrap.

The emulator accepted a clean console shutdown. Final checks found no ADB
endpoint or target emulator process. Network state was never changed. The
three preserved `ApexPhase9Lab` host-file hashes and timestamps match the
preflight exactly. Raw output remains local-only.

## Required result

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED WHPX(10.0.26200) INSTALLED_AND_USABLE; EXIT_CODE_0
AVD_BOOT_COMPLETED = YES
GPU_MODE = host

SYSTEMUI_PREFLIGHT_STABLE = NO

PACKAGE_STATE_VALID = NOT_REACHED
OBB_STATE_VALID = NOT_REACHED
VALIDATION_CACHE_REUSED = NOT_REACHED

GAMEACTIVITY_RESUMED = NO_CLIENT_NOT_LAUNCHED

GLES_COMPAT_DIALOG_PRESENT = NOT_OBSERVED_CLIENT_NOT_LAUNCHED
GRAPHICS_GATE_PASSED = UNKNOWN_NOT_TESTED

OPENGL_RUNTIME_STAGE = NOT_REACHED
RHI_CONTINUATION_EVIDENCE = NO_NEW_EVIDENCE

SCREEN_STATE_5S = NOT_CAPTURED
SCREEN_STATE_15S = NOT_CAPTURED
SCREEN_STATE_30S = NOT_CAPTURED
SCREEN_STATE_60S = NOT_CAPTURED
SCREEN_STATE_120S = NOT_CAPTURED

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

FINAL_GATE = G SYSTEMUI_PRELAUNCH_ANR_STOP
COMMIT = Validate host GLES31 Apex progression Phase15O
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

The result is a precondition stop, not a graphics failure. Phase15N remains
authoritative for the clean-room host GLES31 capability; Phase15L remains
authoritative for the compatibility dialog under its earlier renderer state.
