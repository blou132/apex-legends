# Boundary

No phone, ADB endpoint, app launch, ptrace operation, runtime instrumentation,
hardware breakpoint, GameProtector interaction, AppData access, network
contact, binary write, Ghidra project write, APK/OBB modification, or Samsung
access occurred.

```text
PHONE_TOUCHED = NO
ADB_USED = NO
APEX_LAUNCHED = NO
PTRACE_USED = NO
ACTIVE_HW_BREAKPOINT_COUNT = 0
GAMEPROTECTOR_INTERFERENCE = NO
APPDATA_TOUCHED = NO
APK_MODIFIED = NO
OBB_MODIFIED = NO
GHIDRA_READ_ONLY = YES
FUTURE_PTRACE_TRACE_GATE = NO_GO
```

All raw scripts, Ghidra logs, pseudo-disassembly, and broad rejected scan output
remain under ignored `Analyse/LocalInputs/Phase16P` storage.
