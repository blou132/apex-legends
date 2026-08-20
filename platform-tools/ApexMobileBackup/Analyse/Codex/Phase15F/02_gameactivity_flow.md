# GameActivity flow

## Targeted DEX subgraph

| Method | DEX address | Relevant behavior |
| --- | --- | --- |
| `GameActivity.onCreate` | `0x505998dc` | Loads OBB metadata and initializes `HasAllFiles`. |
| `GameActivity.onStart` | `0x5059b964` | Starts ordinary input/GCloud lifecycle; no downloader decision. |
| `GameActivity.onResume` | `0x5059b5dc` | Calls `onResumeBody` unless a force-end warning/error is active. |
| `GameActivity.onResumeBody` | `0x50594624` | Branches on `HasAllFiles`. |
| `GameActivity$17.run` | `0x50590200` | Constructs the downloader intent and calls `startActivityForResult`. |
| `DownloadShim.GetDownloaderType` | `0x5058fa04` | Returns the concrete `DownloaderActivity` class. |
| `GameActivity.onActivityResult` | `0x50599458` | Decodes downloader result and updates `HasAllFiles`. |

## Initialization condition

`onCreate` sets `HasAllFiles=true` only when package data is inside the APK or
the package declares no OBB files. Neither is true here. It performs its direct
file-presence precheck only when `VerifyOBBOnStartUp=false`; the manifest sets
that option to `true`, so `HasAllFiles` intentionally remains false until the
dedicated activity verifies the OBBs.

`onResumeBody` therefore calls `nativeOnInitialDownloadStarted`, posts
`GameActivity$17`, and that runnable resolves `DownloaderActivity`, adds intent
flag `0x00010000`, and invokes:

```text
startActivityForResult(downloader_intent, 0x13881)
```

Phase15D directly records `Game oRB HAF:false` followed by `Game sAFR DOWN` and
the system activity-start event. This confirms the static edge at runtime.

```text
DOWNLOADER_LAUNCH_CALLER = GameActivity$17.run at 0x50590200
DOWNLOADER_LAUNCH_METHOD = startActivityForResult
DOWNLOADER_LAUNCH_CONDITION = HasAllFiles == false
```
