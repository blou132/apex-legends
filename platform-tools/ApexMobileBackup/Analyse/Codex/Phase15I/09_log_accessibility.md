# Log accessibility

## Classification

| Sink | Storage class | Shell-readable without root |
| --- | --- | --- |
| Android logcat | System log service | Yes for permitted application/system output |
| Historical SDK log directory | Shared/public application tree | Yes, established by prior read-only inventory |
| Private application files | App-private | No evidence of shell access |
| Crash artifacts | Mixed SDK-managed locations | Unknown without a specific public path |
| UE4 core file log | Unknown | Unknown |

Phase8 recorded 94 public files, including exactly eight files in an SDK log
directory. Those files were deliberately excluded from collection and content
search. Their presence proves a historical public file-sink witness, but not
their contents or usefulness for named client stages.

No ADB accessibility test was performed in Phase15I.

```text
OFFICIAL_LOG_SINK_SHELL_READABLE = YES_FOR_HISTORICAL_PUBLIC_SDK_FILES_AND_LOGCAT
HISTORICAL_PUBLIC_LOG_WITNESS = YES_SDK_LOG_DIRECTORY_8_FILES
```
