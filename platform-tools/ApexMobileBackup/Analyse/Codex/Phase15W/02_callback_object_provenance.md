# Callback object provenance

The bounded constructor and assignment chain is:

1. `CreatePuffer` at Ghidra `0x005045d0` allocates the 0x20-byte concrete
   facade and calls constructor `FUN_005045ac`.
2. `FUN_005045ac` assigns primary vtable `0x009784c0` at `this+0x00` and
   secondary vtable `0x009785f8` at `this+0x08`.
3. RTTI identifies the concrete type as `GCloud::GCloudPufferImp`. Its two bases
   are `GCloud::IGcloudPufferInterface` and `cu::IPufferCallBack`; the latter has
   offset flags `0x802`, confirming the `this+0x08` secondary subobject.
4. `FUN_0050323c`, the facade Init method, stores its externally supplied client
   callback at `GCloudPufferImp+0x18` (`0x005032e0`).
5. The same Init method writes `this+0x08` into the first field of the config
   passed to the internal manager (`0x005037f0`-`0x005037f8`).
6. `FUN_004f02dc` (`CPufferMgrImp::Init`) loads that first field at
   `0x004f0650` and passes it to `FUN_004f42d4`.
7. `FUN_004f42d4`, the `CPufferActionCallBackImp` constructor, stores the pointer
   at scheduler offset `+0x08` at `0x004f431c`.
8. `FUN_004f4418` later passes that exact field to `ProcessResult`.

The distinct result field `CPufferInitActionResult+0x08` comes from
`CPufferInitAction+0x38` and points to `CPufferMgrImpInter`. It is not the
`cu::IPufferCallBack` object.

```text
CALLBACK_OBJECT_CONSTRUCTOR = FUN_005045ac via CreatePuffer
CALLBACK_OBJECT_ASSIGNMENT_SITE = FUN_0050323c and FUN_004f42d4
CALLBACK_OBJECT_OWNER = GCloud::GCloudPufferImp, retained by CPufferActionCallBackImp
```
