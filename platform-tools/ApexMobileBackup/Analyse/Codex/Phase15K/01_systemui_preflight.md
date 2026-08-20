# SystemUI preflight

The first post-boot window/activity inspection showed the launcher resumed and
no SystemUI ANR. A valid baseline hierarchy was then captured.

Before Apex launch, a second inspection found a visible, on-screen,
wrap-content system-alert window titled `Application Not Responding:
com.android.systemui`. It remained present after a passive 90-second wait.

No action was sent to that window. SystemUI was not force-stopped or restarted,
and no secure setting was changed. The required stop condition therefore
applied before any application launch.

```text
SYSTEMUI_ANR_PRESENT_PRELAUNCH = YES
SYSTEMUI_OVERLAY_CLEAR = NO
PREFLIGHT_WAIT_SECONDS = 90
APEX_LAUNCHED = NO
```
