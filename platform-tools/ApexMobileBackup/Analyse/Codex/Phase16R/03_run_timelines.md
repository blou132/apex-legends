# Normalized run timelines

All times are relative to the corresponding application/process start.

## Phase15U

| Relative time | Relevant event |
|---:|---|
| +0.0 s | Game activity start |
| +116.6 s | GCloud/Puffer manager initialization |
| +116.7 s | Puffer offline wait |
| +118.175 s | `version_mgr_imp.cpp` Init |
| +118.278 s | Dolphin `NormalConnectVersionSvr` begins |
| +118.347 s | Version result path |
| +118.416 s | Action error processing |

## Phase16I warm-up

| Relative time | Relevant event |
|---:|---|
| about +0 s | Process/native startup |
| about +34.1 s | GCloud/Puffer initialization |
| about +55.3 s onward | Puffer timeout/retry sequence |
| +150 s | Bounded run ends; no Dolphin/version-manager witness |

## Phase16I trace

| Relative time | Relevant event |
|---:|---|
| +0.0 s | Activity launch |
| about +0.4 s | `libgcloud.so` load |
| early window | Retained trace setup completed |
| +90 s | Bounded run ends; no Dolphin/version-manager witness |

## Phase16M correlated launch

| Relative time | Relevant event |
|---:|---|
| +0.0 s | Game activity start |
| about +0.6 s | `libgcloud.so` load |
| about +2.5 s | Downloader activity starts |
| about +3.0 s | Downloader activity completes |
| about +9.6 s | GCloud/Puffer initialization |
| later bounded window | No Dolphin/version-manager witness; no active observation |

No retained run contains an exact first-extract-success, flag-set, or Init
failure marker.
