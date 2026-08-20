# Runtime boundary

## Preflight

- WHPX acceleration was usable.
- The existing AVD booted with no snapshot load/save and no wipe.
- Package version and code matched the Phase15H baseline.
- Both OBB names and sizes matched.
- The validation cache contained the two expected rows, each matching its OBB
  modification value.
- Airplane mode was enabled, Wi-Fi and mobile data were disabled, the active
  default network was absent, and no default route existed before launch.

## One bounded launch

The downloader selected files-present state `4`, returned result `1` about
`0.257 s` after its creation, and `GameActivity` resumed. No full-validation
marker appeared. The explicit `Down POB 0` text was not captured, but the valid
unchanged cache, state `4`, immediate result, and absence of full validation
confirm cache reuse.

The process stayed alive with Apex windows present at +5, +15, +30, +60, +120,
and +180 seconds post-resume. The run stopped at the required bound.

```text
VALIDATION_CACHE_REUSED = YES
GAMEACTIVITY_RESUMED = YES
PROCESS_ALIVE_180S = YES
```
