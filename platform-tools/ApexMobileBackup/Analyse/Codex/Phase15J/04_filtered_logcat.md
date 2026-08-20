# Filtered logcat

Logcat was cleared immediately before the one Apex launch. The broad raw stream
remains local-only. Cleaned counts for the established useful tags are:

| Tag | Rows |
| --- | ---: |
| `UE4` | 2 |
| `GCloudCore` | 189 |
| `GCloud` | 27 |
| `TDM` | 85 |
| `[MSDK]` | 56 |
| `MSDK` | 9 |
| `[CrashSightReport]` | 2 |
| `PluginMSDK` | 2 |

The only application splash message is the previously known short splash-start
witness. Android also reports its separate launcher splash window exiting.
There is no video-completion or `DismissSplashScreen` message.

No requested Lua, ClientLaunch, EventSystem, LoginMgr, avatar server-list, or
event `0x138` term appears in the bounded logcat.

Raw process fields, identifiers, private paths, request bodies, and URL queries
are excluded from all committed material.
