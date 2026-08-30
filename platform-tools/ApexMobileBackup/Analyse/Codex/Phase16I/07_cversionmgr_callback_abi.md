# CVersionMgr callback ABI

The exact function-entry disassembly proves:

- `X0` is saved as the `CVersionMgrImp` instance;
- `X1` is saved as an argument container;
- the callback object is loaded from `*(uint64_t *)X1`;
- that callback is stored at `CVersionMgrImp+0x18`.

The Phase16A dispatch was also revalidated. `FUN_0050cb38` loads the external
callback from `CVersionStrategy+0x18`, reads the callback vptr, then calls the
function pointer at vtable slot `+0x28`.

```text
THIS_REGISTER = X0
EXTERNAL_CALLBACK_ARGUMENT_REGISTER = X1
CALLBACK_ARGUMENT_INTERPRETATION = DEREFERENCE_X1_ONCE
CALLBACK_STORE_SITE = ELF_0x004765cc
CALLBACK_STORE_SEMANTIC = CVersionMgrImp+0x18
CALLBACK_ARGUMENT_REGISTER_PROVEN = YES
EXTERNAL_SLOT_28_DISPATCH_REVALIDATED = YES
```
