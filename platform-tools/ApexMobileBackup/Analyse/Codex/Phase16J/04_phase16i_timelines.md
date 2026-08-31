# Phase16I timelines

## Warm-up

| Relative time | Event |
|---:|---|
| about +0 s | Process startup and native logs begin |
| about +2.7 s | DownloaderActivity starts |
| about +26.7 s | DownloaderActivity finishes |
| about +34.1 s | GCloud/Puffer initialization |
| about +55.3 s | First Puffer timeout and retry |
| about +76.5 s | Second timeout and manager restart |
| about +118.9 s | Further timeout/restart |
| about +140 s | Further Puffer timeout |
| +150 s | Bounded warm-up ends |

Puffer activity and retries were directly observed. No version-manager or
Dolphin version-action witness was present in the bounded raw log.

## Active trace launch

| Relative time | Event |
|---:|---|
| +0.0 s | Activity launch |
| about +0.4 s | `libgcloud.so` load call |
| about +2.2 s | DownloaderActivity starts |
| about +2.5 s | DownloaderActivity finishes |
| early process window | Exact Thread-10 selected after module mapping; hardware breakpoint installed and read back |
| +90 s | Trace timeout with no trap and no version-manager/Dolphin log witness |
| after timeout | Breakpoint disabled, detached, and Apex force-stopped |

The breakpoint was installed well before the historical Phase15U
version-manager window near +118 s. This weakens H4. The warm-up itself also
contained no Init witness, so H1 is not supported.

```text
PHASE16I_WARMUP_PUFFER_WITNESS = OBSERVED
PHASE16I_WARMUP_VERSIONMGR_WITNESS = NOT_OBSERVED
PHASE16I_WARMUP_DOLPHIN_WITNESS = NOT_OBSERVED
PHASE16I_TRACE_VERSIONMGR_WITNESS = NOT_OBSERVED
PHASE16I_TRACE_DOLPHIN_WITNESS = NOT_OBSERVED
BREAKPOINT_INSTALLED_BEFORE_EXPECTED_TRIGGER_WINDOW = YES
```
