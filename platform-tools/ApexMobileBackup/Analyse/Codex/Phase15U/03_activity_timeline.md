# Activity and process timeline

Relative times use the single `GameActivity` start as `t0`.

| Relative time | Cleaned observation |
| ---: | --- |
| `t0` | Android starts Apex `GameActivity` and its process. |
| about `+3.6 s` | `GameActivity` reaches its Android resume callback. |
| about `+3.9 s` | `DownloaderActivity` starts. |
| about `+4.1 s` | `DownloaderActivity` finishes by application request. |
| `+5/+15/+30/+60 s` | Apex process remains present; checkpoint dumps retain `GameActivity`. |
| about `+109 s` | `GameActivity` resumes again and its surfaces draw. |
| about `+117 s` | The client launch level and update-related initialization are logged. |
| `+120 s` | Rendered update error is captured while `GameActivity` is resumed. |
| `+180 s` | Process remains present until the bounded controller force-stops it. |

The quick downloader completion, continued `GameActivity` execution, later
OBB-backed media access, and rendered update UI establish progression beyond
the expansion-file transition. This run did not emit the exact
`HasAllFiles=true` or `nativeResumeMainInit` strings, so those exact messages
are not claimed as new Phase15U witnesses.

```text
GAMEACTIVITY_STARTED = YES
DOWNLOADERACTIVITY_STARTED = YES
GAMEACTIVITY_RESUMED = YES
OBB_DOWNLOADER_TRANSITION_PASSED = CONFIRMED_OPERATIONALLY
HAS_ALL_FILES_EXACT_LOG = NO_NEW_EVIDENCE
NATIVE_RESUME_MAIN_INIT_EXACT_LOG = NO_NEW_EVIDENCE
```
