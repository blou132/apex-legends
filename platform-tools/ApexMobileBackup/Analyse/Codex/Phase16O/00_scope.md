# Scope

Phase16O is a PC-only, static, read-only audit of the exact preserved
`libUE4.so`. It analyzes only ELF `0x05a311a8...0x05a31ca8`, the
`CreateDolphin` callsite at `0x05a314cc`, and the already bounded direct caller
chain needed to classify that callsite.

The existing Ghidra project was reused read-only with analysis disabled. Raw
instruction listings, helper scripts, and Ghidra exports remain local-only.

No phone, ADB, Apex launch, ptrace, runtime trace, GameProtector interaction,
AppData access, network access, binary modification, or broad indirect-call
scan occurred.

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
```
