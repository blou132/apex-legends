# Splash exit runtime

Runtime evidence contains the known Java splash-start message but no video
completion, Java dismissal, or native dismissal trigger.

The Android launcher splash-window exit is a separate operating-system window
transition and must not be confused with dismissal of the APK `VideoView`
splash established in Phase15I.

The application splash/dialog windows remain present through the final +180
second checkpoint. Neither official SDK logs nor logcat connect TDM or GCloud
failures to the Java dismissal thunk.

```text
SPLASH_VIDEO_COMPLETION_RUNTIME = NO_NEW_EVIDENCE
SPLASH_DISMISS_RUNTIME = NO_NEW_EVIDENCE
NATIVE_SPLASH_DISMISS_TRIGGER = UNKNOWN
```
