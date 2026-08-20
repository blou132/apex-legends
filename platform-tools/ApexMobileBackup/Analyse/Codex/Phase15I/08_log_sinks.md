# Log sinks

| Log sink | Path form | Writer evidence | Rotation | Enabled condition |
| --- | --- | --- | --- | --- |
| Android logcat | System-managed | Java and native SDK tags observed | System-managed | Runtime logging and per-framework filtering |
| GCloudCore file logs | Public application data/cache roots supplied at runtime | `AFileLogger` and open/write worker evidence | Old-file cleanup present | Logger initialized in observed client run |
| MSDK XLog | Default SDK log path under application storage | XLog worker and default-path construction | Framework-managed | Logger initialized in observed client run |
| TDM binary/KV logs | SDK-selected application log location | Named TDM writers/files compiled | Unknown | TDM logging active in observed run |
| CrashSight artifacts | SDK crash-report location | Crash/minidump mechanisms compiled | SDK-managed | Crash or report condition |
| UE4 core file log | No exact path proved | No targeted writer chain proved | Unknown | Unknown |

No conventional UE4 file path is invented. The exact public SDK directory name
and individual historical filenames are intentionally omitted because they are
not required for the cleaned conclusion.

```text
OFFICIAL_LOG_SINK_FOUND = YES_SDK_FILE_LOGS_AND_SYSTEM_LOGCAT
OFFICIAL_LOG_SINK_TYPE = SDK_FILE_LOGS_PLUS_SYSTEM_LOGCAT
```
