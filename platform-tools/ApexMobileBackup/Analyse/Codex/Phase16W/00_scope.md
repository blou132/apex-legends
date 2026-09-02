# Phase16W scope

This phase is PC-only, static, and read-only. It maps the exact callback
parameter of `GCloudDolphinImp::Init` back to the real client Init call, then
tests only the proven callback candidate, `DolphinCallback+0x08`, and the
independent `GameUpdateMgr` selected-entry `+0x38` control.

No phone, ADB, runtime launch, ptrace, breakpoint, network, AppData, binary
modification, writable Ghidra operation, global displacement scan, generic
constructor scan, or whole-callgraph scan was used.

```text
PHONE_TOUCHED = NO
ADB_USED = NO
APEX_LAUNCHED = NO
PTRACE_USED = NO
ACTIVE_HW_BREAKPOINT_COUNT = 0
GAMEPROTECTOR_INTERFERENCE = NO
APPDATA_TOUCHED = NO
NETWORK_USED = NO
```
