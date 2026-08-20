# Phase15D runtime correlation

Times are relative to creation of the sole Apex process. Absolute timestamps,
process identifiers, endpoint details, and raw log text are intentionally not
published.

| Relative time | Source | Sanitized classification |
| ---: | --- | --- |
| +12.410 s | `GameActivity` | `onResume` entered. |
| +12.787 s | `GameActivity` | `onResumeBody`; `HasAllFiles=false`. |
| +13.758 s | Android activity manager | `GameActivity` displayed. |
| +13.774 s | `GameActivity` | Starts downloader for result. |
| +13.859 s | Android activity manager | `DownloaderActivity` start accepted. |
| +14.028 s | `GameActivity` | `onPause` entered. |
| +14.259 s | `DownloaderActivity` | `onCreate` entered. |
| +14.519 s | `DownloaderActivity` | Internal state `4`: all OBBs present in one location. |
| +14.543 s | `DownloaderActivity` | `ProcessOBBFiles` branch `1`: verify on startup and cache not current. |
| +15.864 s | Android activity manager | `DownloaderActivity` displayed. |
| +17.064 s | Android window manager | `GameActivity` window hidden behind downloader UI. |
| +18.121 s | `GameActivity` | `onStop` entered. |
| +61.066 s | Apex process | Process still active after the bounded window. |

No downloader completion, validation post-execute, result callback,
DownloaderActivity teardown, Java exception, native fatal signal, missing-file
state, download-service transition, or storage-permission failure appears in
the existing bounded log.

Progress callbacks do not emit a line for every ZIP entry, so the quiet period
does not distinguish slow progress from a stuck worker. The strongest direct
statement is that full local validation started and no completion was captured
before observation ended.

```text
OBSERVED_NEXT_TRANSITION_AFTER_60S = NONE; DOWNLOADER_REMAINS_FOREGROUND_AND_PROCESS_ACTIVE
FIRST_BLOCKING_CONDITION = CONFIRMED_VALIDATION_PENDING; NON_COMPLETION_CAUSE_UNKNOWN
```
