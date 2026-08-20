# Validation timeline

Times below are relative to creation of the single Apex process. Absolute
timestamps and process identifiers are omitted.

| Relative time | Cleaned witness |
| ---: | --- |
| +0.000 s | Apex process created. |
| +14.015 s | `GameActivity.onResume` entered. |
| +14.699 s | `onResumeBody` recorded `HasAllFiles=false`. |
| +15.427 s | Android reported `GameActivity` displayed. |
| +15.522 s | `GameActivity` started the downloader-for-result path. |
| +15.617 s | Android accepted `DownloaderActivity` start. |
| +15.915 s | `DownloaderActivity.onCreate` entered. |
| +16.074 s | State `4` confirmed all OBBs present in one location. |
| +16.075 s | `ProcessOBBFiles` selected full local validation. |
| +17.323 s | Android reported `DownloaderActivity` displayed. |
| about +46 s | +30 s validation checkpoint: process alive, downloader foreground. |
| about +77 s | +60 s validation checkpoint: process alive, downloader foreground. |
| +104.001 s | Validator `onPostExecute` reported success. |
| +104.472 s | `GameActivity.onActivityResult` recorded result `1`, `HasAllFiles=true`. |
| +104.475 s | `GameActivity.onResume` entered again. |
| +104.606 s | `onResumeBody` recorded `HasAllFiles=true`. |

The exact interval from full-validation selection to its success callback is
87.926 seconds. This directly answers the Phase15G question: the unchanged
local validator does complete after 60 seconds.

```text
DOWNLOADER_ACTIVITY_OBSERVED = YES
VALIDATION_COMPLETED = YES
VALIDATION_COMPLETION_TIME = +87.926S_AFTER_FULL_VALIDATION_SELECTION
```
