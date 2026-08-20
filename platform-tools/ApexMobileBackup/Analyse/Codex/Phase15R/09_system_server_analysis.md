# system_server analysis

`system_server` consumed `60%` CPU in the ANR interval, mostly in kernel mode,
but ActivityManager remained able to detect the timeout, schedule trace
collection, emit the exact ANR record, start other processes, and continue
service management. This is not evidence of a globally stalled system_server.

The Keyguard service record shows an asynchronous service completion still
pending (`binder=null`, `requested=true`, `received=false`, execution active).
No trace proves a synchronously blocked system_server thread waiting in Binder
on SystemUI. A later bulk bugreport unwind of system_server timed out under the
same loaded environment, so no stronger thread-level claim is made.

```text
SYSTEM_SERVER_HEALTH = RESPONSIVE_HIGH_CPU
SYSTEM_SERVER_WAITING_FOR_SYSTEMUI = YES_ASYNC_SERVICE_COMPLETION_PENDING; NO_SYNC_BLOCK_PROVED
SYSTEM_SERVER_ROLE = TIMEOUT_DETECTOR_AND_SERVICE_LIFECYCLE_TRACKER
```
