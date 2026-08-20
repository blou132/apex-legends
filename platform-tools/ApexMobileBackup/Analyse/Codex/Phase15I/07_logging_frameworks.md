# Logging framework inventory

| Framework | Compiled evidence | Observed initialization | Sink class |
| --- | --- | --- | --- |
| Android `Log` via UE4 Java logger | DEX methods call Android logging APIs | Splash-start message observed before Shipping suppression | System logcat |
| GCloudCore file logger | File logger, open, rotation, cleanup, and worker strings | File-log module and cleanup worker initialized | SDK file log plus logcat |
| MSDK XLog | Default log-path and XLog worker strings | XLog worker and log module initialized | SDK file log plus logcat |
| TDM logging | Named binary/KV log files in exact library | Runtime log level active | SDK file log plus logcat |
| CrashSight | Crash report, minidump, and log-path mechanisms | SDK initialized | Crash artifacts and SDK telemetry |
| UE4 native file output | No exact `Saved/Logs`, project log, or file-output witness in targeted binary search | Not observed | Unknown |

Crash reports are not classified as a continuous client-stage log. The SDK
file loggers are official code shipped in the exact client, but their content
is not assumed to contain UE4, Lua, or Login stage events.

```text
FILE_LOGGING_COMPILED = YES_FOR_SDK_LOGGERS; UNKNOWN_FOR_UE4_CORE
FILE_LOGGING_ENABLED_BY_DEFAULT = YES_FOR_OBSERVED_GCLOUD_MSDK_LOGGERS; UNKNOWN_FOR_UE4_CORE
```
