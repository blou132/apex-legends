# Result and return flow

## Contract

`GameActivity$17.run` starts the downloader with request code `0x13881`
(`80001`). `DownloaderActivity` uses Android `RESULT_OK` (`-1`) plus an intent
extra named `Result`.

| Extra value | GameActivity meaning | HasAllFiles |
| ---: | --- | --- |
| `0` | no return code | false; force quit |
| `1` | files present/validated | true |
| `2` | download completed OK | true |
| `3` | user quit | false; force quit |
| `4` | download failed | false; force quit |
| `5` | expansion invalid | false; force quit |
| `6` | no Play key | false; force quit |

For the Phase15D branch, CRC success returns `1`. `GameActivity.onActivityResult`
(`0x50599458`) calls `nativeOnInitialDownloadCompleted`, sets
`HasAllFiles=true` for results `1` or `2`, and records the downloader error
code. Android then resumes `GameActivity`; `onResumeBody` sees
`HasAllFiles=true`, calls `nativeResumeMainInit`, and marks initialization
complete.

## Edge confidence

| Edge | Confidence |
| --- | --- |
| Downloader success -> `setResult(-1, Result=1)` -> `finish()` | `CONFIRMED` static; observed in Phase10 |
| `onActivityResult(0x13881)` -> `HasAllFiles=true` | `CONFIRMED` static; observed in Phase10 |
| Activity finish -> Android resumes GameActivity | `CONFIRMED` framework path; observed in Phase10 |
| `onResumeBody` -> `nativeResumeMainInit` when files are ready | `CONFIRMED` static; observed in Phase10 |
| Phase15D reaches downloader success/result/native resume | `UNKNOWN` (not present in bounded log) |

```text
REQUEST_CODE = 0x13881 / 80001
RESULT_CODES = 0_NO_CODE, 1_FILES_PRESENT, 2_DOWNLOAD_OK, 3_USER_QUIT, 4_FAILED, 5_INVALID, 6_NO_PLAY_KEY
SUCCESS_RESULT = ANDROID_RESULT_OK_WITH_EXTRA_1_OR_2
FAILURE_RESULT = EXTRA_0_OR_3_TO_6
```
