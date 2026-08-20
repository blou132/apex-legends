# Phase15F - DownloaderActivity and OBB transition

Date: 2026-08-20

## Executive result

The exact APK again matches 96,228,800 bytes and the authoritative SHA256.
Its manifest identifies `GameActivity` as the sole launcher and
`DownloaderActivity` as a non-exported, same-process helper activity.

The exact DEX path is now resolved. Because expansion data is outside the APK,
OBBs are declared, and startup verification is enabled, `GameActivity` leaves
`HasAllFiles=false`. On resume it posts `GameActivity$17`, which starts
`DownloaderActivity` for result with request `0x13881`.

Phase15D then follows the files-present path. Both expected OBB names and exact
outer sizes are accepted in the normal production directory. A stale or absent
app-private validation cache selects full asynchronous validation: each ZIP
entry carrying a CRC is decompressed/read and checked with CRC32. This path is
fully local and does not require Google Play or network access.

Phase10 observed the identical path and byte-identical OBBs complete, return
result `1`, set `HasAllFiles=true`, and call `nativeResumeMainInit`. Phase15D
only captured the same validator starting; no completion or result callback
appears before the 60-second bound. The expected next transition is therefore
known, but the reason validation did not finish within that separate window is
unknown.

`DownloaderActivity` owns the foreground window during the reported black
interval. Its resources define a non-empty verification/progress layout, so
timing does not prove an empty or transparent UI failure. Without pixels, the
downloader link is probable while the final black-screen cause remains unknown.

Phase15F performed no AVD boot, Apex launch, ADB/phone operation, network
request, artifact change, download, root, patch, hook, or bypass. Raw APK/DEX,
OBBs, logs, and targeted Ghidra output remain local-only.

## Required results

```text
GAMEACTIVITY_IS_LAUNCHER = YES
DOWNLOADERACTIVITY_IS_LAUNCHER = NO

DOWNLOADER_LAUNCH_CALLER = GameActivity$17.run at DEX 0x50590200
DOWNLOADER_LAUNCH_CONDITION = HasAllFiles == false after mandatory startup OBB verification

EXPECTED_OBB_DIRECTORY = /storage/emulated/0/Android/obb/com.ea.gp.apexlegendsmobilefps
MAIN_NAME_MATCH = YES
PATCH_NAME_MATCH = YES
OBB_CHECKS = EXISTS + EXACT_OUTER_SIZE + CACHE_LAST_MODIFIED + PER_ZIP_ENTRY_CRC32
OBB_VALIDATION_EXPECTED = SUCCESS_FOR_IDENTICAL_PHASE10_BYTES

DOWNLOADER_INITIAL_STATE = FILES_PRESENT_THEN_FULL_LOCAL_VALIDATION
DOWNLOADER_OFFLINE_BEHAVIOR = VALIDATES_PRESENT_OBBS_WITHOUT_NETWORK
DOWNLOADER_SUCCESS_PATH = RESULT_1 -> HAS_ALL_FILES_TRUE -> RESUME -> NATIVE_RESUME_MAIN_INIT

PHASE10_PATH_COMPARISON = SAME_EXPECTED_PATH
ANDROID16_INCOMPATIBILITY = NO_EVIDENCE

BLACK_SCREEN_DOWNLOADER_LINK = PROBABLE_TEMPORAL_AND_FOREGROUND_OWNERSHIP_LINK
FIRST_BLOCKING_CONDITION = CONFIRMED_VALIDATION_PENDING_AT_60S; ROOT_CAUSE_UNKNOWN

FINAL_GATE = E DOWNLOADER_TRIGGER_RESOLVED_BLOCKER_UNKNOWN
COMMIT = Resolve downloader transition Phase15F
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence limit

Phase15F proves the activity trigger, path lookup, validation algorithm,
offline behavior, result contract, and expected return. It does not prove
validator completion in Phase15D, identify why that run exceeded 60 seconds,
show downloader pixels, or establish a final black-screen cause.
