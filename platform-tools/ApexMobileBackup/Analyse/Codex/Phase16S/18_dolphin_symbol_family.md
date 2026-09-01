# Dolphin dynamic-symbol family

The named-family audit found 29 Ghidra symbol records: four UE4 imports and 25
gcloud export records. Aliases collapse the provider records to 15 canonical
addresses, for 19 canonical consumer/provider entries overall.

UE4 imports:

- `CreateDolphin`
- `ReleaseDolphin`
- `GCloud::DolphinHelper::GetCurApkPath`
- `GCloud::DolphinHelper::InstallAPK`

| Consumer import | Provider | UE4 relocation/GOT | Reachable exact callers |
| --- | --- | --- | ---: |
| `CreateDolphin` | `libgcloud.so` | `R_AARCH64_JUMP_SLOT`, ELF `0x0b37e8c8` | 0; one exact dead callsite |
| `GetCurApkPath` | `libgcloud.so` | `R_AARCH64_JUMP_SLOT`, ELF `0x0b37e8d0` | one bounded Shutdown-adjacent call previously proven |
| `ReleaseDolphin` | `libgcloud.so` | `R_AARCH64_JUMP_SLOT`, ELF `0x0b37e8d8` | 0 |
| `InstallAPK` | `libgcloud.so` | not reconstructed by this named-symbol pass | not recounted |

Canonical gcloud exports include Create/Release Dolphin and utility variants,
four path/install/copy helpers, dynamic parameter adjustment, and RTTI/vtables
for `GCloudDolphinInterface` and `GCloudDolphinUtility`.

The current Ghidra reference-manager pass reports zero caller functions for
the external import records. The table therefore preserves the stronger exact
results already established by the bounded Phase16L/Phase16P analyses instead
of treating the reference-manager result as a global negative. Prior exact
analysis classifies UE4's known CreateDolphin caller as dead/unreachable; it
was not reopened. Path/install helpers are not acquisition functions, and
Release functions are teardown.

`DOLPHIN_SYMBOL_FAMILY_COUNT = 29`

`DOLPHIN_ACQUISITION_SYMBOL_CANDIDATES = NONE_AFTER_REACHABILITY_FILTER`
