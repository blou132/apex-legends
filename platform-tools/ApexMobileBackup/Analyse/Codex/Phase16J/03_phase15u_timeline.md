# Phase15U timeline

Relative to the observed GameActivity start:

| Relative time | Event |
|---:|---|
| +0.0 s | GameActivity start |
| +3.6 s | Android resume |
| +3.9 s | DownloaderActivity starts |
| +4.1 s | DownloaderActivity finishes |
| +109.1 s | GameActivity resumes and surfaces return |
| +116.6 s | GCloud/Puffer manager initialization |
| +116.7 s | Puffer offline connection wait begins |
| +118.175 s | `version_mgr_imp.cpp` Init witness |
| +118.278 s | Dolphin `NormalConnectVersionSvr` begins |
| +118.347 s | Version update result path |
| +118.416 s | Action error processing |
| about +120 s | Visible update progress/error state |
| +137.745 s | First Puffer timeout |
| +158.965 s | Second timeout and retry |

The Puffer initialization preceded the version-manager witness, but the
static analysis shows that Puffer is not itself the direct Init caller.

```text
PHASE15U_VERSION_MGR_INIT_WITNESS = OBSERVED
PHASE15U_VERSION_MGR_INIT_RELATIVE_TIME = +118.175 s
PHASE15U_DOLPHIN_WITNESS = OBSERVED
PHASE15U_PUFFER_BEFORE_VERSIONMGR = YES
```
