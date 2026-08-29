# Future Apex trace design

The preserved future static target remains:

```text
MODULE = libgcloud.so
FUNCTION = CVersionMgrImp::Init
GHIDRA_IMAGE_ADDRESS = 0x00576180
DOWNSTREAM = CVersionStrategy_Win32 slot+0x00 -> FUN_0050cb38
CALLBACK_SLOT = external callback object slot+0x28
```

A future authorized runtime phase would first derive the module-relative
offset from the exact same `libgcloud.so`, then combine it with that run's
actual module base. It must not reuse an absolute ASLR address.

Phase16I's intended observation remains the callback object passed to
`CVersionMgrImp::Init` and its vtable/slot+0x28 target. This phase did not
install or launch Apex and did not validate an instrumentation method.

```text
SELECTED_PHASE16I_METHOD = UNRESOLVED
```
