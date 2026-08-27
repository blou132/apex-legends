# Package debug and profile policy

The installed package is still version code `64003140`, version
`1.3.672.546`, matching the package previously decoded in Phase15E.

Current package flags contain neither `DEBUGGABLE` nor a profileability flag.
`run-as` explicitly rejects the package as non-debuggable. The exact decoded
APK manifest from Phase15E contains no `<profileable>` element and no
`profileableByShell` opt-in. Android 8 also predates the modern shell
profileability path used by newer platform profiling tools.

```text
APEX_DEBUGGABLE = NO
APEX_PROFILEABLE = NO
APEX_PROFILEABLE_BY_SHELL = NO
APEX_RUN_AS_AVAILABLE = NO
APEX_JDWP_AVAILABLE_WHEN_RUNNING = NO_BY_NONDEBUGGABLE_POLICY
```

No process launch was used to reach these conclusions.
