# Phase15D runtime timeline

All times are relative to the main Apex process creation (`t=0`). Absolute log
times and process identifiers are intentionally omitted.

| Relative time | Attributed source | Observation |
| ---: | --- | --- |
| -0.215 s | Android system | Captured log begins. |
| -0.181 s | system_server | Apex launch intent accepted. |
| 0.000 s | Apex main process | Process created. |
| +0.134 s | Apex main process | Berberis translation runtime initialized. |
| +0.624 s | Apex main process | ARM64 application native-loader namespace configured. |
| +1.663 s | Apex main process | First Apex ARM64 library load: `libanort.so`. |
| +3.430 s | Apex render thread | GFXSTREAM opens GLES emulation libraries. |
| +10.494 s | Apex main process | First successful `libUE4.so` load. |
| +10.603 s | Apex main process | UE4-tagged splash startup logged. |
| +11.029 s | Apex main process | GCloud remote-config cache reported absent. |
| +11.049 s | Apex main process | SDK application `onCreate` lifecycle observed. |
| +11.278 s | Apex worker | First application request: TDM HTTP POST. |
| +12.069 s | Apex worker | First GCloud RemoteConfig request starts. |
| +12.408 s | Apex main process | Application `onStart` observed. |
| +12.788 s | Apex main process | Application `onResume` observed. |
| +13.758 s | system_server | `GameActivity` reported displayed. |
| +13.859 s | system_server | Apex `DownloaderActivity` started. |
| +15.864 s | system_server | `DownloaderActivity` reported displayed. |
| +17.064 s | Apex main process | `GameActivity` window becomes hidden behind downloader UI. |
| +59.517 s | Apex worker | Last TDM retry in the bounded window fails at DNS. |
| +61.066 s | Apex main process | Background GC confirms the process is still active. |

Only one Apex process was observed. `DownloaderActivity` ran in that process.
Finsky/Google Play, input method, system_server, and other SDK/system messages
were attributed separately and were not promoted to Apex events.

```text
LOG_START = -0.215S_RELATIVE_TO_PROCESS
PROCESS_START = 0.000S
FIRST_APEX_LIBRARY_LOAD = LIBANORT_SO_AT_+1.663S
LIBUE4_LOAD = +10.494S
FIRST_APPLICATION_REQUEST = TDM_POST_AT_+11.278S
LAST_OBSERVED_STAGE = DOWNLOADER_ACTIVITY_DISPLAYED; PROCESS_ACTIVE_AT_+61.066S
```
