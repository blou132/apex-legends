# Security and Apex boundary

```text
APEX_INSTALLED = NO
APEX_LAUNCHED = NO
APK_OR_OBB_COPIED = NO
SAMSUNG_ACCESSED = NO
SELINUX_PERMISSIVE_USED = NO
SEPOLICY_MODIFIED = NO
MAGISK_MODULE_INSTALLED = NO
ZYGISK_ENABLED_BY_PHASE = NO
PERSISTENT_DEBUG_DAEMON = NO
ANDROID_SYSTEM_MODIFIED = NO
NETWORK_OR_BACKEND_ACTIVITY = NO
```

Only the disposable tracees were attached. The software probe attempted one
temporary instruction write that failed. The hardware probes modified only
per-thread debug state on disposable processes, both of which were terminated
when exact restoration could not be proven.
