# Client forward path

The resolved state path is:

```text
FUN_00550ee4 NormalConnectVersionSvr
  -> FUN_00549800 error relay
  -> cu::CActionMgr vtable 0x0097b000 slot +0x00
  -> FUN_005bf94c OnActionError
  -> FUN_005bf808 queue insertion
  -> FUN_005befa0 scheduler
  -> FUN_005be71c ProcessActionError
  -> callback at CActionMgr+0x3b8, slot +0x00 (unresolved)
```

RTTI and vtable results:

- action class: `dolphin::gcloud_version_action_imp`
- action primary vtable: Ghidra `0x009797d0`, ELF `0x008797d0`
- manager class: `cu::CActionMgr`, derived from `cu::IActionCallback`
- manager vtable: Ghidra `0x0097b000`, ELF `0x0087b000`
- manager constructor `FUN_005bfb10` initializes the external result callback at
  offset `0x3b8` to null

The callback assignment and concrete slot target are not present in the bounded
direct path. Per the stop rule, the chain ends at this indirect dispatch.

The `version_mgr_imp.cpp` source anchor does not resolve a concrete version
manager RTTI object, vtable, or error-callback implementation in this bounded
path. The resolved `dolphin::gcloud_version_action_imp` action must not be
promoted as the version manager itself.

PROCESSACTIONERROR_FORWARD_CHAIN = raw queue code -> callback CActionMgr+0x3b8 slot +0x00 -> UNKNOWN
DOLPHIN_VERSION_MANAGER_CLASS = UNKNOWN
DOLPHIN_VERSION_MANAGER_VTABLE = UNKNOWN
DOLPHIN_VERSION_MANAGER_ERROR_CALLBACK = UNKNOWN
DOLPHIN_ERROR_FORWARDING_STYLE = RAW
QTCVFS_PUFFER_PATH_SEPARATE = YES
