# Client boundary

Source/monitor strings identify `FUN_00576180` as
`CVersionMgrImp::Init` and `FUN_00576890` as
`CVersionMgrImp::CheckAppUpdate`. No manager RTTI name is promoted beyond this
direct source evidence.

`CVersionMgrImp::Init` receives an external callback through its argument and
stores it at `CVersionMgrImp+0x18`. Registration copies this pointer to
`CVersionStrategy+0x18`. The resolved slot-00 implementation then dispatches
that object's virtual slot `+0x28`.

The concrete external callback class, vtable, and slot `+0x28` target are not
recoverable from this bounded static dataflow. The callback chain therefore
crosses an external client interface but does not prove `libUE4.so` ownership.

```text
CLIENT_VERSION_MANAGER_CLASS = CVersionMgrImp (source/monitor evidence)
CLIENT_VERSION_MANAGER_FUNCTION = FUN_00576180 Init; FUN_00576890 CheckAppUpdate
CALLBACK_BOUNDARY = EXTERNAL_CLIENT
LIBUE4_EXACT_CALLBACK_ANCHOR = NONE
LIBUE4_CALLBACK_CONSUMER = UNKNOWN
```
