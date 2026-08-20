# Phase15I - Lightspeed wait and application log sinks

Date: 2026-08-20

## Executive result

Phase15I used only retained local evidence. No ADB endpoint, emulator, phone,
application run, or network operation was used.

The Phase15H image previously described as a Lightspeed wait state is a stack
of Android windows. The Lightspeed logo is the exact APK's raw splash video,
inflated by `GameActivity` into an Android `VideoView`. The visible `Wait`
button belongs instead to a SystemUI ANR dialog that predates the Apex splash.
A later Apex-owned Android dialog shows an `OK` action, but its obscured message
and creator remain unknown. No UE4-rendered splash or wait asset is required by
the evidence.

The confirmed Java splash exit is
`GameActivity.AndroidThunkJava_DismissSplashScreen()`. The build disables
automatic removal, so video completion alone only records completion and calls
native code. An explicit native-triggered Java dismissal is required; the exact
native function and state predicate remain unknown. No direct control-flow edge
links TDM, GCloud, Login, Lua, or server-list readiness to that dismissal.

The exact client also ships official SDK file loggers. GCloudCore and MSDK file
logging initialize in the retained run, and TDM file writers are compiled. A
prior read-only public inventory saw eight SDK log files in the application's
public tree. This establishes a shell-readable official application file sink
without root or patching, although no evidence yet shows that it contains named
UE4/Lua/Login stage logs. A targeted UE4 core file log remains unproved.

## Required results

```text
WAIT_UI_OWNER = ANDROID
WAIT_UI_VISUALLY_CHANGES = YES_BETWEEN_5S_AND_30S; NO_BETWEEN_30S_AND_120S
WAIT_UI_PROGRESS_VISIBLE = NO

APK_WAIT_UI_MATCH = YES_FOR_LIGHTSPEED_SPLASH; NO_FOR_SYSTEM_WAIT_DIALOG
UE4_SPLASH_ASSET_CANDIDATE = NO_EVIDENCE_APK_VIDEO_CONFIRMED
UE4_WAIT_UI_ASSET_CANDIDATE = NO_EVIDENCE_ANDROID_DIALOGS_CONFIRMED

WAIT_UI_CREATOR = GAMEACTIVITY_ONCREATE_FOR_SPLASH; ANDROID_SYSTEM_FOR_ANR_WAIT; UNKNOWN_FOR_APEX_OK_DIALOG
WAIT_UI_EXIT_FUNCTION = GAMEACTIVITY_ANDROIDTHUNKJAVA_DISMISSSPLASHSCREEN_CONFIRMED
WAIT_UI_EXIT_CONDITION = NATIVE_EXPLICIT_DISMISS_REQUIRED; EXACT_NATIVE_TRIGGER_UNKNOWN

TDM_WAIT_DEPENDENCY = NO_EVIDENCE
GCLOUD_WAIT_DEPENDENCY = NO_EVIDENCE

FILE_LOGGING_COMPILED = YES_FOR_SDK_LOGGERS; UNKNOWN_FOR_UE4_CORE
FILE_LOGGING_ENABLED_BY_DEFAULT = YES_FOR_OBSERVED_GCLOUD_MSDK_LOGGERS; UNKNOWN_FOR_UE4_CORE
OFFICIAL_LOG_SINK_FOUND = YES_SDK_FILE_LOGS_AND_SYSTEM_LOGCAT
OFFICIAL_LOG_SINK_TYPE = SDK_FILE_LOGS_PLUS_SYSTEM_LOGCAT
OFFICIAL_LOG_SINK_SHELL_READABLE = YES_FOR_HISTORICAL_PUBLIC_SDK_FILES_AND_LOGCAT

HISTORICAL_PUBLIC_LOG_WITNESS = YES_SDK_LOG_DIRECTORY_8_FILES

NAMED_CLIENT_STAGE_LOG_TAGS_FOUND = NO
TARGET_LOG_TAGS_FOR_FUTURE_RUN = UE4,GCloudCore,GCloud,TDM,[MSDK],MSDK,[CrashSightReport],PluginMSDK

FIRST_BLOCKING_DEPENDENCY = UNKNOWN
NEXT_RUNTIME_METHOD = READ_OFFICIAL_CLIENT_LOG_ONLY

FINAL_GATE = B OFFICIAL_READABLE_APPLICATION_LOG_SINK_FOUND
COMMIT = Resolve splash wait and logging sinks Phase15I
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence limit

Phase15I does not identify the native caller or condition that dismisses the
splash, the message behind the obscured Apex `OK` dialog, a first Lua/Login
stage, or a named client-stage log tag. It confirms SDK logging availability,
not the presence of gameplay or authentication data in those logs.
