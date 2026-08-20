# Downloader trigger

## Exact launch trigger

The downloader did not start because Android selected it as the launcher. It
started because mandatory startup verification left `HasAllFiles=false`, then
`GameActivity.onResumeBody` posted the downloader request.

Inside `DownloaderActivity.onCreate` (`0x5058d548`), the Phase15D path is also
exactly identified:

1. `expansionFilesDelivered` found both fixed OBB entries with exact lengths.
2. `onlySingleExpansionFileFound` found no duplicate production/development
   pair and returned true.
3. Runtime marker `Down 4` selected `ProcessOBBFiles` (`0x5058ce78`).
4. Runtime marker `Down POB 1` proves `VerifyOBBOnStartUp=true` and
   `expansionFilesUptoData=false`.
5. The activity started asynchronous full ZIP-entry CRC validation.

`expansionFilesUptoData=false` means the app-private `cacheFile.txt` did not
contain matching last-modified values for every expected OBB. An absent cache
is probable on this first install; the runtime marker alone proves the broader
cache-absent-or-stale condition.

## Candidate classification

| Candidate | Classification | Reason |
| --- | --- | --- |
| Main OBB absent | `CONFIRMED_NOT_TRIGGER` | `Down 4` requires all declared OBBs to pass presence/size checks. |
| Patch OBB absent | `CONFIRMED_NOT_TRIGGER` | Same check covers both entries. |
| Incorrect outer file size | `CONFIRMED_NOT_TRIGGER` | `doesFileExistInternal` requires the declared size. |
| Incorrect OBB/version filename | `CONFIRMED_NOT_TRIGGER` | Fixed version/package names passed the lookup. |
| Download state or prior download | `CONFIRMED_NOT_TRIGGER` | The Google downloader service branch was not selected. |
| Startup expansion verification | `CONFIRMED_TRIGGER` | Manifest verification keeps `HasAllFiles=false`. |
| Validation cache absent/stale | `CONFIRMED_TRIGGER` | `Down POB 1` selects full CRC validation. |
| OBB path inaccessible | `CONFIRMED_NOT_TRIGGER` | Both files were opened through the normal location check. |
| External storage unavailable | `CONFIRMED_NOT_TRIGGER` | Presence/size checks succeeded. |
| Saved activity state | `CONFIRMED_NOT_TRIGGER` | No branch on saved state controls this decision. |
| Runtime storage permission denial | `NO_EVIDENCE` | No denial or exception appears in the bounded log. |
| Network unavailable | `CONFIRMED_NOT_TRIGGER` | The selected local-validation path does not start the download service. |

```text
WHY_DOWNLOADER_ACTIVITY_STARTED = MANDATORY_STARTUP_OBB_VERIFICATION_WITH_HAS_ALL_FILES_FALSE
FIRST_WAIT_STATE = FULL_ZIP_ENTRY_CRC_VALIDATION_AFTER_CACHE_FRESHNESS_MISS
```
