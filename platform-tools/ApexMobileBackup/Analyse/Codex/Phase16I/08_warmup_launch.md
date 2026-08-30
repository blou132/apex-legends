# Offline warm-up launch

The resolved launcher activity was started offline without a breakpoint. Apex
remained alive for the bounded 150-second observation and was then force-stopped.
`libgcloud.so` appeared near process startup. Thread count rose from roughly 60
to roughly 100 and then stabilized.

The native caller thread later identified by Android as `Thread-10` performed
GCloud and Puffer initialization. Puffer retries and offline DNS failures were
visible. The current warm-up did not emit `version_mgr_imp.cpp` or Dolphin
version-action lines.

No credentials were entered and no backend interaction was attempted.

```text
APEX_LAUNCHED_OFFLINE = YES
LIBGCLOUD_RUNTIME_LOAD_OBSERVED = YES
THREAD_INVENTORY_AVAILABLE = YES
WARMUP_NETWORK_ISOLATION_MAINTAINED = YES
```
