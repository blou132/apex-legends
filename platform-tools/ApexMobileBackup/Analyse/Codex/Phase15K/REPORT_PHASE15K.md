# Phase15K - Apex dialog hierarchy preflight

Date: 2026-08-20

## Executive result

No physical Android device was present. WHPX was usable and the unchanged
`ApexPhase9Lab` AVD booted without wipe, reinstall, cache changes, root, or
snapshot load/save.

The UI hierarchy mechanism was validated before launch: a compressed guest XML
was pulled successfully and parsed as a well-formed four-node launcher
hierarchy. Package version `1.3.672.546` / code `64003140`, both expected OBB
names and byte sizes, and the two-row OBB validation cache all matched.

The first window preflight was clear. Before Apex launch, however, a visible
`Application Not Responding: com.android.systemui` system-alert window appeared
and remained after a passive 90-second wait. The required preflight gate was
therefore enforced. Apex was never launched, and no system or application
dialog was clicked, dismissed, or otherwise manipulated.

The guest network had already been isolated and was restored exactly to its
initial state. The AVD then shut down cleanly with no ADB endpoint or emulator
process remaining. Phase15K does not resolve the Apex dialog text, creator,
trigger, role, blocking status, or splash relation.

## Required results

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES

SYSTEMUI_ANR_PRESENT_PRELAUNCH = YES
SYSTEMUI_OVERLAY_CLEAR = NO
UI_DUMP_BASELINE_WORKS = YES

VALIDATION_CACHE_REUSED = NOT_TESTED_NO_APEX_LAUNCH
GAMEACTIVITY_RESUMED = NO

APEX_DIALOG_REPRODUCED = NO_NOT_LAUNCHED
APEX_DIALOG_APPEAR_TIME = NOT_APPLICABLE
APEX_DIALOG_VISUAL_CONFIRMED = NO_NOT_LAUNCHED

DIALOG_PACKAGE = UNKNOWN_NOT_CAPTURED
DIALOG_CLASS = UNKNOWN_NOT_CAPTURED
DIALOG_TITLE = UNKNOWN
DIALOG_MESSAGE = UNKNOWN
DIALOG_BUTTONS = UNKNOWN_IN_PHASE15K

DIALOG_TEXT_RESOURCE = UNKNOWN
DIALOG_CREATOR_CLASS = UNKNOWN
DIALOG_CREATOR_METHOD = UNKNOWN
DIALOG_TRIGGER_CONDITION = UNKNOWN

APEX_OK_DIALOG_ROLE = UNKNOWN
DIALOG_BLOCKS_BOOTSTRAP = UNKNOWN
USER_ACK_REQUIRED = UNKNOWN

DIALOG_CONTROLS_SPLASH_DISMISS = UNKNOWN
NATIVE_SPLASH_DISMISS_TRIGGER = UNKNOWN

FIRST_BLOCKING_DEPENDENCY = UNKNOWN_APPLICATION_NOT_STARTED

NETWORK_RESTORED = YES
AVD_SHUTDOWN_CLEAN = YES

FINAL_GATE = F SYSTEMUI_OVERLAY_PRESENT_STOP
COMMIT = Resolve Apex dialog hierarchy Phase15K
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence limit

This phase confirms capture capability and enforces the SystemUI preflight
boundary. It provides no new Apex runtime evidence. Phase15J remains
authoritative for reproduction of the third Apex-owned window, while its exact
text and code path remain unresolved.
