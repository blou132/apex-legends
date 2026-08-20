# Phase15G - bounded OBB CRC completion observation

Date: 2026-08-20

## Executive result

The mandatory preflight found no physical Android device. WHPX was usable and
the unchanged `ApexPhase9Lab` AVD booted without snapshot load/save or wipe.
The existing Apex package, version, code, and both OBB names/sizes matched; no
artifact was reinstalled, rehashed in the guest, or modified.

The targeted validation cache was absent before launch, so no prior-success
proof was available. The guest was placed into the same strict offline state as
Phase15D, with no active default network or default route. Apex was launched
once with no account or login input.

`DownloaderActivity` again selected files-present state `4` and full local
validation. At both +30 and +60 seconds the process was alive and downloader
remained foreground. The validator then reported success 87.926 seconds after
full validation selection. The activity returned exact result `1`;
`GameActivity.onActivityResult` set `HasAllFiles=true`, and `GameActivity`
resumed. The process stayed stable for 30 additional seconds without the
downloader returning.

The same targeted cache check then found two valid rows whose OBB names and
last-modified values matched both installed files. No CRC, ZIP, I/O, exception,
or process failure occurred. The log contains no new direct
`nativeResumeMainInit` name witness.

Apex was force-stopped, guest network settings were restored exactly, and the
AVD shut down cleanly. No endpoint or emulator process remained. Raw runtime
data remains local-only.

## Required results

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES

PACKAGE_STATE_VALID = YES
OBB_STATE_VALID = YES

VALIDATION_CACHE_PREFLIGHT = ABSENT
VALIDATION_CACHE_CONTENT_VALID = UNKNOWN
PREVIOUS_VALIDATION_SUCCESS_PROVEN = NO

NETWORK_ISOLATION_CONFIRMED = YES

APEX_LAUNCHED = YES_ONCE
DOWNLOADER_ACTIVITY_OBSERVED = YES

VALIDATION_COMPLETED = YES
VALIDATION_COMPLETION_TIME = +87.926S_AFTER_FULL_VALIDATION_SELECTION
VALIDATION_FAILURE_CLASS = NONE

DOWNLOADER_RESULT = 1
GAMEACTIVITY_RESUMED = YES
NATIVE_RESUME_MAIN_INIT_WITNESS = NO_NEW_EVIDENCE

VALIDATION_CACHE_POSTRUN = PRESENT
VALIDATION_CACHE_CONTENT_VALID_POSTRUN = YES

PROCESS_STABLE_AFTER_SUCCESS = YES_30S

NETWORK_RESTORED = YES
AVD_SHUTDOWN_CLEAN = YES

FINAL_GATE = B CURRENT_VALIDATION_COMPLETES_AND_GAMEACTIVITY_RESUMES
COMMIT = Observe bounded OBB validation completion Phase15G
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence limit

Phase15G resolves only the bounded local OBB validation question. It does not
establish a native runtime address, profiling result, Lua/Login stage, backend
response, gameplay frame, or final black-screen cause.
