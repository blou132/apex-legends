# Android storage and network compatibility

## Static network requirement

The Phase15D path can validate already-present OBB files entirely offline. The
Google expansion downloader service is reached only from the missing-file
branch and was not selected: runtime state `Down 4` proves that both declared
files passed the production/development location and exact-length checks.

If files were missing, the service state machine could pause for unavailable
network, Wi-Fi, roaming, or storage. Those states do not explain the observed
local-validation branch.

```text
STATIC_NETWORK_REQUIREMENT = A LOCAL_OBB_VALIDATION_IS_FULLY_OFFLINE
GOOGLE_PLAY_CONTACT = CONDITIONAL_ON_MISSING_FILES; NOT_SELECTED_IN_PHASE15D
```

## Storage APIs

The downloader constructs the legacy shared-storage path through
`Environment.getExternalStorageDirectory()` plus `/Android/obb/<package>`.
The manifest targets SDK 32, requests legacy external storage, declares
READ/WRITE external-storage permissions, and sets `bUseExternalFilesDir=true`.
The validation cache itself is stored below `getExternalFilesDir(null)`.

No `SDK_INT` or `Build.VERSION` branch controls the observed OBB lookup,
presence check, cache decision, or ZIP-entry CRC validation. No
`MANAGE_EXTERNAL_STORAGE` branch was found in this subgraph.

Most importantly, Phase15D directly records the files-present state on the
Android 16 guest. This proves that the application resolved and inspected both
expected production OBB paths in that environment. It does not prove every
possible Android version is supported, but it rules out path invisibility as
the trigger for this run.

```text
SDK_VERSION_BRANCH_PRESENT = NO_IN_OBSERVED_DOWNLOADER_SUBGRAPH
STORAGE_API_USED = LEGACY_EXTERNAL_STORAGE_ANDROID_OBB_PLUS_APP_EXTERNAL_FILES_CACHE
ANDROID16_INCOMPATIBILITY = NO_EVIDENCE
```
