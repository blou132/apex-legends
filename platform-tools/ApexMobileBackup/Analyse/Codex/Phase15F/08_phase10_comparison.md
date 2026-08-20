# Phase10 comparison

Phase10 and Phase15D use the same DEX control flow and the same expected OBB
identities. In both runs, `GameActivity` reaches the downloader because startup
verification leaves `HasAllFiles=false`; `DownloaderActivity` then finds both
files and selects local validation rather than the Play download service.

| Boundary | Phase10 | Phase15D |
| --- | --- | --- |
| Both expected OBBs found | confirmed | confirmed by `Down 4` |
| Validation cache current | no | no, confirmed by `Down POB 1` |
| Full ZIP-entry CRC validation starts | confirmed | confirmed |
| Validation completes | about +27.2 s in that run | not logged by +60 s |
| Downloader returns `Result=1` | confirmed | not observed |
| `HasAllFiles=true` | confirmed | not observed |
| `nativeResumeMainInit` | called and returned | not observed after downloader launch |

The local artifacts used in Phase15D are byte-identical to the OBBs that
passed Phase10. The code therefore predicts the same success path, but a prior
successful run is not proof that the asynchronous validator completed during
the separate bounded Phase15D observation.

There is no branch contradiction. The only observed divergence is elapsed
progress: Phase10 captured completion, whereas Phase15D ended while the same
validation stage was still active. The reason for that longer duration is not
logged and remains unknown.

```text
PHASE10_PATH = FILES_PRESENT -> FULL_CRC_VALIDATION -> RESULT_1 -> HAS_ALL_FILES_TRUE -> NATIVE_RESUME_MAIN_INIT
PHASE15D_OBSERVED_PATH = FILES_PRESENT -> FULL_CRC_VALIDATION_PENDING_AT_END_OF_WINDOW
PHASE10_PATH_COMPARISON = SAME_EXPECTED_PATH
```
