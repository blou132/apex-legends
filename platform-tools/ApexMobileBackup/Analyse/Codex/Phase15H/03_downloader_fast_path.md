# Downloader fast path

`GameActivity` briefly opened same-process `DownloaderActivity`. The cleaned
lifecycle sequence was:

| Process-relative time | Evidence |
| ---: | --- |
| `+15.004 s` | Android starts `DownloaderActivity`. |
| `+15.639 s` | `Down onCreate`. |
| `+15.835 s` | `Down 4`: OBB files-present state. |
| `+15.852 s` | `Down POB 2`: current-cache branch. |
| `+15.961 s` | `GameActivity` receives result `1`. |
| `+15.962 s` | `GameActivity.onResume` begins. |
| `+16.231 s` | resume body completes with `HasAllFiles=true`; post-resume T0. |

The activity returned result `1` in `0.957 s` from its Android start and was
destroyed `1.567 s` after that start. There was no long CRC-validation interval
or CRC progress. Together with the valid preflight cache and `Down POB 2`, this
confirms reuse of the previous successful validation cache.

```text
DOWNLOADER_ACTIVITY_OBSERVED = YES
VALIDATION_CACHE_REUSED = YES
DOWNLOADER_START_TIME = +15.004S_PROCESS_RELATIVE
DOWNLOADER_FINISH_TIME = +15.961S_PROCESS_RELATIVE_RESULT_RETURN
DOWNLOADER_DURATION = 0.957S_TO_RESULT_RETURN
DOWNLOADER_DESTROY_TIME = +16.571S_PROCESS_RELATIVE
DOWNLOADER_RESULT = 1
GAMEACTIVITY_RESUMED = YES
```
