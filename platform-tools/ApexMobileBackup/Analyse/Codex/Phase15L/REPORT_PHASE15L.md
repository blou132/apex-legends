# Phase15L - Clean preflight retry and Apex dialog capture

Date: 2026-08-20

## Executive result

No physical Android device was present. WHPX was usable, and the unchanged
`ApexPhase9Lab` AVD completed a clean boot without wipe or snapshots. SystemUI
remained free of ANRs across the required 120-second settle window and the final
prelaunch check. The baseline UI hierarchy was valid, package/OBB/cache identity
matched, and offline network isolation was confirmed.

The single authorized Apex launch reused the established downloader fast path
and resumed `GameActivity`. A third Apex-owned wrap-content window appeared at
`+15.391 s` post-resume. Its valid five-node hierarchy resolves the title,
complete message, and sole `OK` button. The dialog reports that the application
requires OpenGL ES 3.1 and floating-point render-target support, while the
device meets neither requirement and the package has no ES2 fallback. Its role
is therefore `DEVICE_COMPATIBILITY`.

Targeted DEX analysis identifies `com.epicgames.ue4.MessageBox01` as the
probable creator. Its `show()` implementation creates a non-cancelable
`AlertDialog`, displays it on the UI thread, and waits until a button handler
records the selection. Explicit acknowledgement is confirmed for this
mechanism, and bootstrap blocking is probable. Exact dialog strings and JNI
method names occur in `libUE4.so`, but the existing read-only Ghidra program
contains no direct string references, so the native trigger caller remains
unknown.

The dialog XML transfer succeeded, but the wrapper interpreted ADB pull progress
on stderr as an exception before the screenshot command. No Phase15L screenshot
exists and Apex was not relaunched. The XML evidence is complete; visual
confirmation for this phase remains `NO`.

Apex was force-stopped, network state was restored exactly, and the AVD shut
down with no endpoint or emulator process remaining.

## Required results

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES

SYSTEMUI_ANR_0S = NO
SYSTEMUI_ANR_30S = NO
SYSTEMUI_ANR_60S = NO
SYSTEMUI_ANR_90S = NO
SYSTEMUI_ANR_120S = NO
SYSTEMUI_PREFLIGHT_STABLE = YES

UI_DUMP_BASELINE_WORKS = YES

VALIDATION_CACHE_REUSED = YES_FAST_PATH_INFERRED_FROM_VALID_CACHE_AND_STATE_4_RESULT_1
GAMEACTIVITY_RESUMED = YES

APEX_DIALOG_REPRODUCED = YES
APEX_DIALOG_APPEAR_TIME = +15.391S_POST_RESUME
APEX_DIALOG_VISUAL_CONFIRMED = NO_XML_ONLY_SCREENSHOT_CAPTURE_FAILED

DIALOG_PACKAGE = com.ea.gp.apexlegendsmobilefps
DIALOG_CLASS = android.app.AlertDialog (PROBABLE; HIERARCHY_ROOT=android.widget.FrameLayout)
DIALOG_TITLE = Unable to run on this device!
DIALOG_MESSAGE = App is packaged for OpenGL ES 3.1 but device has not met all the requirements for ES 3.1 :\n\tDevice has OpenGL ES 3.1 support: NO\n\tFloating point render target support: NO\nThis device only supports OpenGL ES 2 but the app was not packaged with ES2 support.
DIALOG_BUTTONS = OK
DIALOG_RESOURCE_IDS = android:id/alertTitle, android:id/message, android:id/buttonPanel, android:id/button1

DIALOG_TEXT_RESOURCE = libUE4.so UTF-16 strings; no Android resource ID
DIALOG_CREATOR_CLASS = com.epicgames.ue4.MessageBox01 (PROBABLE)
DIALOG_CREATOR_METHOD = createAlert()/show() (PROBABLE)
DIALOG_TRIGGER_CONDITION = PROBABLE_GLES31_AND_FLOAT_RENDER_TARGET_REQUIREMENTS_NOT_MET_WITH_NO_ES2_FALLBACK

APEX_DIALOG_ROLE = DEVICE_COMPATIBILITY
USER_ACK_REQUIRED = YES
DIALOG_BLOCKS_BOOTSTRAP = PROBABLE
DIALOG_CONTROLS_SPLASH_DISMISS = NO_EVIDENCE

NATIVE_SPLASH_DISMISS_TRIGGER = UNKNOWN
FIRST_BLOCKING_DEPENDENCY = OPENGL_ES_3_1_DEVICE_COMPATIBILITY_GATE

NETWORK_RESTORED = YES
AVD_SHUTDOWN_CLEAN = YES

FINAL_GATE = B APEX_DIALOG_TEXT_RESOLVED_TRIGGER_UNKNOWN
COMMIT = Retry clean Apex dialog capture Phase15L
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

Phase15L supersedes Phase15J/K for the Apex dialog content and role. It does not
establish the native trigger caller, a direct edge to splash dismissal, a
readable runtime mapping/load bias, Lua/Login reachability, or a gameplay frame.
Raw runtime and static artifacts remain local-only.
