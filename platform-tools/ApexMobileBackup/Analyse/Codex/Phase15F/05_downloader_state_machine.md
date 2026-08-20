# DownloaderActivity state machine

## Activity-level states

`DownloaderActivity.onCreate` initializes `layout/downloader_progress`, then
selects one of four application states:

| Internal state | Condition | Behavior |
| ---: | --- | --- |
| `1` | OBB missing and no Play public key | Non-cancelable error dialog, then result `6`. |
| `2` | OBB missing and Play download service available | Start/connect downloader service and show download UI. |
| `3` | Both production and development copies exist | Ask which copy to use, then process it. |
| `4` | OBB files present in one location | Check validation cache; validate CRCs if stale. |

Phase15D is confirmed in internal state `4`, not state `2`.

## Validation state

`onPreExecute` exposes the verification dashboard, sets the status to
`Verifying Download`, installs a cancel-verification action, and hides the
Wi-Fi/download controls. Progress callbacks update fraction, percent, speed,
remaining time, and the progress bar without emitting a log line.

On success, `onPostExecute` writes current OBB last-modified values to
`getExternalFilesDir(null)/cacheFile.txt`, returns result `1`, and finishes.
On CRC failure it deletes the cache, shows `XAPK File Validation Failed.`, and
offers an action that returns result `5`.

## Download-service states

Only the missing-file branch uses the Google expansion downloader service.
Its client states cover idle, URL lookup, connecting, downloading, completed,
paused/no-network/Wi-Fi/roaming/storage conditions, and terminal failure. State
`5` (`completed`) transitions into the same local ZIP CRC validator.

`onStart` connects the client stub when present; `onServiceConnected` registers
the client messenger; `onStop` disconnects it. `onPause` changes a still-empty
result to user-quit (`3`). `onDestroy` cancels validation cooperatively.

```text
DOWNLOADER_STATES = FILE_CHECK, LOCAL_VALIDATION, DOWNLOAD_SERVICE, PAUSED, FAILURE, SUCCESS_RETURN
INITIAL_STATE = FILE_CHECK_THEN_LOCAL_VALIDATION
OFFLINE_STATE = NOT_APPLICABLE_TO_PHASE15D_LOCAL_VALIDATION; PAUSED_NETWORK_UNAVAILABLE_IF_FILES_MISSING
SUCCESS_STATE = RESULT_1_AFTER_LOCAL_VALIDATION
FAILURE_STATE = RESULT_5_AFTER_CRC_FAILURE; OTHER_DOWNLOAD_FAILURES_MAP_TO_RESULT_4_OR_6
```
