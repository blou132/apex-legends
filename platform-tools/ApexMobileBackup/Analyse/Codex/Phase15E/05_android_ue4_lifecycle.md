# Android and UE4 lifecycle

## libUE4 context

The first successful `libUE4.so` loader event occurs at `+10.494 s`. A second
successful loader call follows at `+10.572 s`, then a UE4-tagged splash start at
`+10.603 s`.

After the load, the Apex process logs generic lifecycle callbacks:

| Relative time | Witness |
| ---: | --- |
| +11.049 s | SDK/application `onCreate` |
| +12.408 s | application `onStart` |
| +12.788 s | application `onResume` |
| +13.758 s | Android reports `GameActivity` displayed |

No exact `nativeResumeMainInit` or `onResumeBody` string is present in this log.
Phase10's earlier Huawei evidence that `nativeResumeMainInit` returns remains
valid, but it is not a new Phase15D runtime witness.

## Subsequent activity

Android starts `DownloaderActivity` at `+13.859 s` and reports it displayed at
`+15.864 s`. `GameActivity` becomes hidden at `+17.064 s`. The current log does
not contain `HasAllFiles` or a downloader result value, so Phase10's earlier OBB
validation result is not reasserted as a Phase15D observation.

```text
LIBUE4_LOAD_RELATIVE_TIME = +10.494S
NATIVE_RESUME_LOG_WITNESS = NO_NEW_EVIDENCE
ANDROID_LIFECYCLE_AFTER_LIBUE4 = ONCREATE_ONSTART_ONRESUME_GAMEACTIVITY_DISPLAYED
RENDER_INIT_EVIDENCE = ANDROID_HWUI_GLES_AND_WINDOW_DRAW_ONLY
```
