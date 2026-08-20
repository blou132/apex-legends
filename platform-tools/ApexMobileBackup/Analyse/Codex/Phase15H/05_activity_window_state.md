# Activity and window state

All eight targeted activity snapshots produced the same application state.

| Post-resume checkpoints | Foreground | Game visible | Downloader visible | Game window |
| --- | --- | --- | --- | --- |
| `+2`, `+5`, `+15`, `+30`, `+60`, `+120`, `+180`, `+300 s` | `GameActivity` | yes | no | on-screen and visible |

Android reported the Apex task visible and requested-visible. `GameActivity`
was top-resumed and the focused application at every checkpoint; it was also
client-visible, reported-drawn, and reported-visible. Its window surfaces were
on-screen and visible.

An Android `ImmersiveModeConfirmation` window was also on-screen and visible.
It visibly covered part of the application in all three screenshots. This is a
system tutorial overlay, not another Apex activity. `DownloaderActivity`
appeared only as the finished last-paused activity after resume.

```text
FOREGROUND_ACTIVITY = GAMEACTIVITY_AT_ALL_CHECKPOINTS
GAMEACTIVITY_VISIBLE = YES
DOWNLOADER_VISIBLE = NO
WINDOW_VISIBLE = YES
WINDOW_FOCUSED = GAMEACTIVITY_FOCUSED_APP_WITH_VISIBLE_SYSTEM_OVERLAY
OTHER_APEX_ACTIVITY_AFTER_RESUME = NONE_OBSERVED
```
