# Runtime log correlation

Times are relative to the first Android start event for the Apex package. Only sanitized component and transition metadata is retained.

| Relative time | Observation | Dependency result |
| --- | --- | --- |
| `+1.727 s` | TDM route POST starts | side worker |
| `+1.742 s` | curl `6`, HTTP `0`, empty response | TDM retry begins |
| `+1.958 s` | GCloud RemoteConfig GET starts | side worker |
| `+1.969 s` | `UnknownHost`, result `3` | 10-second retry policy |
| about `+2.0 s` | GCloud services and inner-plugin startup continue | first failures are nonfatal |
| about `+2.5 s` | `DownloaderActivity` starts | local expansion verification |
| about `+2.7 s` | both OBBs found; validation begins | no Play download path |
| about `+27.2 s` | validation returns true, `Result=1` | OBB gate passes |
| about `+27.3 s` | GameActivity sets `HasAllFiles=true` | main init may resume |
| about `+27.3 s` | `nativeResumeMainInit` is called | last confirmed game-bootstrap handoff |

GCloud retries continue at roughly 10-second intervals and terminate after retry count reaches zero. TDM retries continue with increasing delays. These timer events do not demonstrate forward game progress or a global wait.

No runtime line names Lua, ClientLaunch, EventSystem, Login, or `RequestAvatarServerList` after the native handoff.
