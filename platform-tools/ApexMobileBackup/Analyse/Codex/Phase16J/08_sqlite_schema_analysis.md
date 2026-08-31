# SQLite schema analysis

Only schema was inspected. No row values were published.

| Database | Tables and columns |
|---|---|
| Data transport | `android_metadata(locale)`; `event_metadata(_id,event_id,name,value)`; `event_payloads(sequence_num,event_id,bytes)`; `events(_id,context_id,transport_name,timestamp_ms,uptime_ms,payload,code,num_attempts,payload_encoding,inline)`; `transport_contexts(_id,backend_name,priority,next_request_ms,extras)` |
| CrashSight | `android_metadata`; `dl_1002`; `ge_1002`; `sqlite_sequence`; `st_1002`; `t_cr`; `t_lr`; `t_pf`; `t_ui` |
| Google measurement | `android_metadata`; `messages(type,entry)` |
| Singular | `android_metadata`; `events(_id,value)`; `sqlite_sequence` |

The schemas identify transport, crash, measurement, and attribution storage.
No table or column directly identifies a Dolphin/CVersionMgr one-time gate.

```text
STATEFUL_DB_CANDIDATES = NONE_DIRECTLY_EVIDENCED
```
