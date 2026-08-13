# Bootstrap dependency graph

Status and `SUCCESS_REQUIRED` refer to the observed application bootstrap, not to each subsystem's own full functionality.

```text
GameActivity / NativeActivity                         CONFIRMED
  -> loads native library named UE4 by manifest      CONFIRMED, success required YES
  -> SDK plugin installation                         CONFIRMED, success required for each UNKNOWN
      -> TDM route/report worker                      CONFIRMED, route success required NO
          -> curl 6 -> timed retry                    CONFIRMED
      -> GCloud RemoteConfig worker                   CONFIRMED, fetch success required NO for plugin startup
          -> UnknownHost -> 3 retries                 CONFIRMED
      -> GCloud/MSDK/GVoice startup continues         CONFIRMED
  -> DownloaderActivity OBB verification             CONFIRMED, OBB result success required YES
      -> both expansion files delivered              CONFIRMED
      -> validation completes, Result=1               CONFIRMED
  -> GameActivity HasAllFiles=true                    CONFIRMED
  -> nativeResumeMainInit                             CONFIRMED invoked and returned
  -> native game initialization                       UNKNOWN
      -> Lua runtime                                  UNKNOWN
      -> ClientLaunch                                 UNKNOWN
      -> Login                                        UNKNOWN
      -> RequestAvatarServerList                      UNKNOWN
```

PlayCommon is excluded because it belongs to Google Play/Finsky, not the Apex process.

The graph confirms that TDM and GCloud requests are parallel/bootstrap side work. It does not identify the state transition that follows `nativeResumeMainInit` inside `libUE4.so`.
