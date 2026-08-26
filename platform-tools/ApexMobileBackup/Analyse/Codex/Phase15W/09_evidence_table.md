# Evidence table

For `libgcloud.so`, `ELF VA = Ghidra address - 0x100000`.

| Claim | Source function | Source address | Call/reference site | Target function/object | Target address | Edge type |
| --- | --- | ---: | ---: | --- | ---: | --- |
| Create facade | `CreatePuffer` | `0x005045d0` | `0x005045e4` | `FUN_005045ac` | `0x005045ac` | `DIRECT_CALL` |
| Assign primary vtable | `FUN_005045ac` | `0x005045ac` | `0x005045b8` | `GCloudPufferImp` primary vtable | `0x009784c0` | `CONSTRUCTOR_ASSIGNMENT` |
| Assign callback-interface vtable | `FUN_005045ac` | `0x005045ac` | `0x005045c8` | `cu::IPufferCallBack` secondary vtable | `0x009785f8` | `CONSTRUCTOR_ASSIGNMENT` |
| Store downstream client callback | `FUN_0050323c` | `0x0050323c` | `0x005032e0` | `GCloudPufferImp+0x18` | n/a | `CONSTRUCTOR_ASSIGNMENT` |
| Put callback subobject in Init config | `FUN_0050323c` | `0x0050323c` | `0x005037f0`-`0x005037f8` | `config[0] = this+0x08` | n/a | `CONSTRUCTOR_ASSIGNMENT` |
| Initialize internal manager | `FUN_0050323c` | `0x0050323c` | `0x00503888` | `FUN_004f02dc` | `0x004f02dc` | `VTABLE_SLOT_RESOLVED` |
| Pass config callback to scheduler ctor | `FUN_004f02dc` | `0x004f02dc` | `0x004f0650`-`0x004f0658` | `FUN_004f42d4` | `0x004f42d4` | `DIRECT_CALL` |
| Store callback in scheduler | `FUN_004f42d4` | `0x004f42d4` | `0x004f431c` | `CPufferActionCallBackImp+0x08` | n/a | `CONSTRUCTOR_ASSIGNMENT` |
| Submit result | `FUN_004fcaf4` | `0x004fcaf4` | `0x004fcb3c` | `FUN_004f3a14` | `0x004f3a14` | `VTABLE_SLOT_RESOLVED` |
| Pass scheduler callback to result | `FUN_004f4418` | `0x004f4418` | `0x004f45c4`-`0x004f45d8` | `FUN_004ee1dc` | `0x004ee1dc` | `VTABLE_SLOT_RESOLVED` |
| Invoke concrete callback slot | `FUN_004ee1dc` | `0x004ee1dc` | `0x004ee308` | `FUN_00503050` | `0x00503050` | `VTABLE_SLOT_RESOLVED` |
| Adjust callback subobject | `FUN_00503050` | `0x00503050` | `0x00503054` | `FUN_00503024` | `0x00503024` | `DIRECT_CALL` |
| Forward to client callback | `FUN_00503024` | `0x00503024` | `0x00503040` | external callback slot `+0x10` | `UNKNOWN` | `VTABLE_SLOT_RESOLVED` |
| Client creates Puffer facade | `FUN_080d1ac8` (`libUE4`) | `0x080d1ac8` | `0x080d1b84` | imported `CreatePuffer` | `0x0a4c1730` thunk / `0x005045d0` libgcloud | `IMPORT_REFERENCE` |

RTTI at `0x00996f50` names `GCloud::GCloudPufferImp`; its base descriptors
name `GCloud::IGcloudPufferInterface` and `cu::IPufferCallBack`. The exact
`libUE4` scalar scan returned zero hits for all five requested values and
therefore promotes no constant edge.
