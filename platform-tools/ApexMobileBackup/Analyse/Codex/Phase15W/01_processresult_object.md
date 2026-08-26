# ProcessResult callback object

`FUN_004ee1dc` is `cu::CPufferInitActionResult::ProcessResult`. Its second
argument supplies the object invoked through virtual slot `+0x10`. The callback
is not stored in the result object.

The confirmed result layout is:

| Offset | Value |
| --- | --- |
| `+0x00` | `cu::CPufferInitActionResult` vtable `0x00977c70` |
| `+0x08` | internal `CPufferMgrImpInter` pointer used only by the success path |
| `+0x10` | success byte |
| `+0x14` | Puffer result/error code |

`FUN_004ee318` assigns all four fields above. It does not assign the callback
used by `ProcessResult`.

`FUN_004f4418` drains a queued result at call site `0x004f45d8`. It loads the
result into `x0` and `CPufferActionCallBackImp+0x08` into `x1`, then invokes the
result vtable slot `+0x10`. This proves the second argument's source.

```text
PROCESSRESULT_CALLBACK_OBJECT = cu::IPufferCallBack subobject of GCloud::GCloudPufferImp
PROCESSRESULT_CALLBACK_FIELD_OFFSET = CPufferActionCallBackImp+0x08; not a CPufferInitActionResult field
PROCESSRESULT_VIRTUAL_SLOT = +0x10
CALLBACK_OBJECT_SOURCE = GCloudPufferImp this+0x08 -> init config[0] -> CPufferActionCallBackImp+0x08
```
