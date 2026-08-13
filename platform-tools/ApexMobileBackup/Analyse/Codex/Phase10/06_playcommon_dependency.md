# PlayCommon dependency

The timestamp request is not emitted by the Apex process. It appears under process ID `11759`, while Apex runs under `12197`. The same `11759` process later logs with the `Finsky` tag that a Google Play package was verified.

The sequence is explicitly log-upload preparation, timestamp lookup, missing account token, then raw log upload. Apex initialization continues independently.

```text
PLAYCOMMON_PROCESS = CONFIRMED GOOGLE_PLAY_FINSKY_PROCESS
PLAYCOMMON_OPERATION = CONFIRMED LOGGING_ONLY
PLAYCOMMON_STARTUP_GATE = CONFIRMED NO
```

This request is removed from the Apex bootstrap dependency graph. No deeper Google Play analysis is needed for this gate.
