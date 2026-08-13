# Startup stall

## Downloader result

The downloader is not the stall. DEX analysis shows `DownloaderActivity::onCreate` checks expected expansion files. Runtime messages select the files-present path, perform validation, return `Result=1`, and destroy the activity normally. `GameActivity::onActivityResult` maps result `1` to `HasAllFiles=true`.

## Last confirmed transition

`GameActivity::onResumeBody` then calls native `nativeResumeMainInit`. The method returns far enough for subsequent Java lifecycle statements to execute. The manifest identifies `android.app.lib_name=UE4`, so this is stronger than a UE4-tagged Java log and confirms the UE4 native handoff. The exact runtime mapping base remains unknown.

After this boundary, the log does not expose an engine state transition, condition variable, callback wait, Lua module load, ClientLaunch, Login, or fatal crash. Repeating TDM/GCloud timers are visible but are already proven not to block the observed handoff.

```text
FIRST_CONFIRMED_STARTUP_STALL = UNKNOWN
LAST_CONFIRMED_TRANSITION = UE4 nativeResumeMainInit returned
STALL_REGION = UNKNOWN inside or after native game initialization
```

Calling the black screen a TDM, RemoteConfig, downloader, Lua, or Login stall would exceed the evidence.
