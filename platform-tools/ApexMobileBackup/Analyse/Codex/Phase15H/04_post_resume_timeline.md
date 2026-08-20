# Post-resume timeline

`POST_RESUME_T0=0` is the completion marker for the resumed `GameActivity`
body with `HasAllFiles=true`.

| Relative time | Cleaned observation |
| ---: | --- |
| `0.000 s` | `GameActivity` post-downloader resume body completes. |
| `+0.109 s` | TDM starts the first post-resume HTTP POST attempt. |
| `+0.172 s` | Native voice SDK reports resume before its own initialization. |
| `+0.200 s` | First post-resume request fails at name resolution. |
| `+0.340 s` | `DownloaderActivity` is destroyed. |
| `+2.065 s` | Apex alive; `GameActivity` foreground and visible. |
| `+5.290 s` | Apex alive; first local-only screen and thread inventory captured. |
| `+16.305 s` | Apex process emits EGL graphics initialization evidence. |
| `+17.127 s` | Apex process searches Vulkan layers. |
| `+30.062 s` | Apex alive; rendered Lightspeed splash and wait UI visible under a system overlay. |
| `+60.077 s` | Apex alive; `GameActivity` remains foreground. |
| `+120.136 s` | Same stable activity and rendered screen state; second thread inventory. |
| `+180.363 s` | Apex alive; `GameActivity` remains foreground. |
| `+300.069 s` | Apex alive; `GameActivity` remains foreground. |

No crash, fatal signal, activity switch, downloader return, or named
Lua/Login/server-list stage was observed.
