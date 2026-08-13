# Next step

## Decision

```text
FINAL_GATE = F JNI_ENTRYPOINT_NOT_RESOLVED
```

The next static pass should start from the exact exported `JNI_OnLoad` function,
if uniquely resolvable, and follow only its class lookup and `RegisterNatives`
branches for `GameActivity`. The goal is to recover a runtime-constructed
registration table or function pointer that does not directly reference the
orphaned `.dynstr` name.

If that path still does not identify one function, a later separately approved
runtime observation may record the resolved native address without changing the
APK or backend. Phase11 itself used no hook, patch, root, ptrace, CA, DNS change,
authentication bypass, or backend emulation.
