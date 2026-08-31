# CVersionMgr callers

Read-only Ghidra analysis used the exact `libgcloud.so` project with
`-noanalysis` and a small address neighborhood.

Static chain:

1. Exported/factory-labeled `CreateDolphin` constructs a `GCloudDolphinImp`
   object whose vtable starts at `0x00979620`.
2. The vtable Init slot at `0x00979630` resolves to `FUN_005458a0`, identified
   by embedded logs as `GCloudDolphinImp::Init`.
3. `GCloudDolphinImp::Init` calls `FUN_00506b14`, which allocates a 0x60-byte
   manager object and installs the vtable at `0x009787c0`.
4. After building the version configuration, Dolphin invokes manager vtable
   slot `+0x10`, stored at `0x009787d0`.
5. That slot is wrapper `FUN_0050690c`, the sole direct code caller of
   `FUN_00576180` (`CVersionMgrImp::Init`).

```text
CVERSIONMGR_INIT_DIRECT_CALLERS = FUN_0050690c
CVERSIONMGR_INIT_CALL_CONTEXT = GCloudDolphinImp::Init -> manager vtable +0x10 -> FUN_0050690c -> FUN_00576180
```
