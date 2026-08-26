# Vtable resolution

## ProcessResult callback

The callback is the `cu::IPufferCallBack` secondary subobject of
`GCloud::GCloudPufferImp`.

| Slot | Ghidra target | Meaning |
| --- | --- | --- |
| `+0x00` | `FUN_00502ff0` | adjusting destructor thunk |
| `+0x08` | `FUN_00503234` | adjusting deleting-destructor thunk |
| `+0x10` | `FUN_00503050` -> `FUN_00503024` | init result forwarder |
| `+0x18` | `FUN_00503080` -> `FUN_00503058` | adjacent callback method |
| `+0x20` | `FUN_005030b4` -> `FUN_00503088` | download result forwarder |

At `0x00503030`, the slot `+0x10` implementation loads the external callback
from `GCloudPufferImp+0x18`; at `0x00503040` it invokes that object's slot
`+0x10`. AArch64 argument registers retain the success flag and error code, so
the error is forwarded without an observed translation.

```text
CALLBACK_INTERFACE_NAME = cu::IPufferCallBack
CALLBACK_CONCRETE_CLASS = GCloud::GCloudPufferImp
CALLBACK_VTABLE_ADDRESS_GHIDRA = 0x009785f8
CALLBACK_VTABLE_ADDRESS_ELF = 0x008785f8
CALLBACK_SLOT_10_TARGET = FUN_00503050 -> FUN_00503024
CALLBACK_SLOT_20_TARGET = FUN_005030b4 -> FUN_00503088
```

## Submission object

`FUN_004fcaf4` submits the result through slot `+0x20` of the object stored at
`CPufferInitAction+0x18`. `StartAction` assigns this field to the action
scheduler, whose RTTI is `cu::CPufferActionCallBackImp` and whose vtable is
`0x009781f0`. Slot `+0x20` resolves to `FUN_004f3a14`, which enqueues the result
in the scheduler queue at `+0x38`.

The submit object and `ProcessResult` callback are therefore different objects:
the scheduler owns the queue and stores the callback interface pointer at
`+0x08`.

```text
SUBMIT_SLOT_20_OBJECT = cu::CPufferActionCallBackImp
SUBMIT_SLOT_20_TARGET = FUN_004f3a14
SAME_MANAGER_OBJECT = NO
```
