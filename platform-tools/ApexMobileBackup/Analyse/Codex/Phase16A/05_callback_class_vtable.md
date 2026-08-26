# Callback class and vtable

Exact RTTI establishes this hierarchy:

```text
cu::IActionMgrCallback
  <- cu::CVersionStrategy
     <- cu::CVersionStrategy_Win32
```

Relevant RTTI objects are at Ghidra `0x00997380`, `0x009973a0`, and
`0x009973b8`. The derived constructor `FUN_0050cedc` installs the
`cu::CVersionStrategy_Win32` primary vtable.

```text
CALLBACK_CLASS = cu::CVersionStrategy_Win32
CALLBACK_INTERFACE = cu::IActionMgrCallback
CALLBACK_VTABLE_GHIDRA = 0x009789b0
CALLBACK_VTABLE_ELF = 0x008789b0
```

The same table places `FUN_0050c4a8` at slot `+0x48`. This links the concrete
strategy object used by `CVersionMgrImp::CheckAppUpdate` to the registration
method that supplies the `CActionMgr` callback.
