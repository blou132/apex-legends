# Guest diagnostics

Only executable presence and guest build policy were inspected. Neither tool
was run against a process.

```text
DEBUGGERD_PRESENT = CONFIRMED /system/bin/debuggerd
SHOWMAP_PRESENT = CONFIRMED /system/bin/showmap
GUEST_DEBUGGABLE = CONFIRMED NO
APEX_INSTALLED = CONFIRMED NO
```

The emulator has a broader diagnostic tool inventory than the Huawei, where
`showmap` was absent. Actual access to a future app process remains untested.
No root, debuggerd backtrace, showmap query, ptrace, package install, uninstall,
or data operation occurred.
