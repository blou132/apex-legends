# Runtime module timing

The warm-up provided the following bounded timing model:

- `libgcloud.so` mapped near startup;
- the native `Thread-10` role became observable before GCloud initialization;
- GCloud/Puffer initialization started about 35 seconds after process start;
- Puffer retried on the same caller thread while worker threads handled transport.

Historical Phase15U evidence independently placed
`version_mgr_imp.cpp::Init` on the same native caller role shortly after Puffer
initialization. The Phase16I warm-up itself did not reproduce that Dolphin
event, so trigger presence remained a trace risk.

```text
LIBGCLOUD_RUNTIME_LOAD_OBSERVED = YES
APEX_PID_BEHAVIOR = SINGLE_STABLE_PROCESS
RELEVANT_CALLER_THREAD_NAME = Thread-10
VERSION_MANAGER_TRIGGER_IN_PHASE16I_WARMUP = NOT_OBSERVED
```
