# Callback provenance

The object stored at `CActionMgr+0x3b8` is the current
`cu::CVersionStrategy_Win32` instance.

Provenance chain:

```text
CVersionMgrImp::Init (FUN_00576180)
  -> FUN_0050cf44 allocates a 0x30-byte strategy object
  -> FUN_0050cedc constructs CVersionStrategy_Win32
  -> CVersionMgrImp+0x10 owns the strategy pointer
  -> CVersionMgrImp::CheckAppUpdate (FUN_00576890)
  -> strategy slot +0x48 (FUN_0050c4a8)
  -> CActionMgr slot +0xe0 (FUN_005bc4bc)
  -> CActionMgr+0x3b8 = strategy this
```

`FUN_0050c714` creates the strategy's manager through
`cu::CActionMgrFactory` slot `+0x10`, resolved to `FUN_00579000`, and stores
the returned manager at `CVersionStrategy+0x20`.

The separate external client callback supplied to `CVersionMgrImp::Init` is
stored at `CVersionMgrImp+0x18`, copied to `CVersionStrategy+0x18` during
registration, and remains opaque.

```text
CALLBACK_PROVENANCE = HEAP_OBJECT
CALLBACK_SOURCE_OBJECT = cu::CVersionStrategy_Win32
CALLBACK_SOURCE_FIELD = CVersionMgrImp+0x10
CALLBACK_SOURCE_FUNCTION = FUN_00576180 -> FUN_0050cf44 -> FUN_0050cedc
```
