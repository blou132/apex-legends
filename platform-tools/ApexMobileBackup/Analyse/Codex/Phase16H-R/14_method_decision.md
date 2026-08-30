# Phase16I method decision

The decision table selects the independent root ptrace path because both Frida
versions are unusable and the minimal native probe successfully attached, read
registers, and detached.

```text
SELECTED_PHASE16I_METHOD = ROOT_PTRACE_NATIVE_DEBUG_PATH
FINAL_GATE = C INDEPENDENT_PTRACE_READY_FRIDA_UNUSABLE
```

This is a method selection, not authorization or readiness to attach to Apex.
Phase16I must not begin until a separate harmless test proves bounded
function-entry and register/argument observation on a disposable ARM64 target.
