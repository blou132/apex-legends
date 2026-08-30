# Active programming result

After all pre-active checks passed, the tracer issued the one authorized atomic
slot-0 programming operation.

```text
APEX_TRACE_ATTEMPT_COUNT = 1
APEX_TRACE_ATTEMPT_LIMIT = ONE
APEX_ACTIVE_SETREGSET_RESULT = SUCCESS
APEX_ACTIVE_ADDRESS_MATCH = YES
APEX_ACTIVE_CTRL_READBACK = 0x000041e4
APEX_ACTIVE_CTRL_MATCH = YES
```

The selected TID was continued for a bounded 90-second window. No hardware
trap arrived. The tracer stopped the thread, disabled slot 0 successfully, and
detached. Phase16I permits no retry.

```text
APEX_SIGTRAP_RECEIVED = NO_TIMEOUT
APEX_SIGTRAP_CODE = NOT_CAPTURED
APEX_SIGTRAP_HW_CLASSIFICATION = NOT_CAPTURED
APEX_SIGTRAP_ADDRESS_MATCH = NOT_CAPTURED
```
