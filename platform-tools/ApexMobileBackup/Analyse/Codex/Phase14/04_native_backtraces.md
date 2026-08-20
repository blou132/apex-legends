# Native backtraces

One backtrace-only request was made during the authorized offline launch. The
operating system returned `debuggerd: failed to dump process` and emitted no
native frame. Per the stop rule, no retry or alternative attach mechanism was
used.

| Snapshot | Result |
| --- | --- |
| Approximately +5 seconds | `FAILED_OS_DIAGNOSTIC_POLICY` |
| Approximately +20 seconds | `NOT_ATTEMPTED_STOP_RULE` |
| Approximately +60 seconds | `NOT_ATTEMPTED_STOP_RULE` |

The failure occurred before a usable backtrace existed. No APK-library frame,
thread name, register set, address, or process identifier is published. Raw
local files remain ignored.

```text
DEBUGGERD_BACKTRACE_AVAILABLE = NO_OS_POLICY
PERSISTENT_LIBUE4_FRAMES = UNKNOWN_NO_BACKTRACE
```
