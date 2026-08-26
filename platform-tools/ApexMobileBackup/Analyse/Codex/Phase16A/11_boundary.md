# Boundary

Phase16A resolves the full internal registration loop:

```text
CVersionMgrImp::Init
  -> heap CVersionStrategy_Win32 at CVersionMgrImp+0x10
  -> CActionMgr at CVersionStrategy+0x20
CVersionMgrImp::CheckAppUpdate
  -> strategy slot +0x48
  -> CActionMgr slot +0xe0
  -> CActionMgr+0x3b8 = CVersionStrategy_Win32
ProcessActionError
  -> callback slot +0x00 = FUN_0050cb38
  -> external client callback at strategy+0x18, slot +0x28
  -> UNKNOWN
```

The exact field-write approach succeeded, so this static axis is not marked as
failed. The remaining boundary is a different object: the callback originally
supplied to `CVersionMgrImp::Init`. Its concrete implementation is opaque in
the bounded path.

```text
CACTIONMGR_STATIC_CALLBACK_AXIS_EXHAUSTED = NO
CURRENT_BLOCKER = UNRESOLVED_EXTERNAL_CLIENT_CALLBACK_AT_CVERSIONSTRATEGY_PLUS_0X18_SLOT_0X28
FINAL_GATE = C CACTIONMGR_CALLBACK_SLOT_00_IMPLEMENTATION_RESOLVED
```
