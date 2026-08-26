# Cross-library registration

The concrete callback invoked by `ProcessResult` is instantiated inside
`libgcloud.so`; it is not a client-defined implementation. Registration occurs
through this internal chain:

```text
GCloudPufferImp::Init (FUN_0050323c)
  config[0] = GCloudPufferImp::cu::IPufferCallBack subobject
  -> CPufferMgrImp::Init (FUN_004f02dc)
  -> CPufferActionCallBackImp constructor (FUN_004f42d4)
  -> scheduler+0x08
```

The facade itself receives a downstream client callback as Init argument 3 and
stores it at `GCloudPufferImp+0x18`. `FUN_00503024` forwards the init result to
that object's virtual slot `+0x10`. This downstream object crosses the client
boundary, but its concrete implementation is unresolved.

The exact `libUE4` registration search found:

- imported `CreatePuffer` with a direct tail-call from `FUN_080d1ac8` at
  `0x080d1b84`;
- no exact `CreatePufferCallBack` import, symbol, or string;
- no exact constructor or registration site for the external callback passed
  to `GCloudPufferImp::Init`.

The `CreatePuffer` call only creates the facade and is not promoted to a
callback registration site.

```text
CALLBACK_REGISTRATION_FUNCTION = FUN_0050323c -> FUN_004f02dc
CALLBACK_REGISTRATION_INTERFACE = cu::IPufferCallBack
CALLBACK_REGISTERED_EXTERNALLY = NO
DOWNSTREAM_CLIENT_CALLBACK_REGISTERED_EXTERNALLY = YES
LIBUE4_CALLBACK_REGISTRATION_SITE = UNKNOWN
LIBUE4_CALLBACK_OBJECT_CONSTRUCTOR = UNKNOWN
LIBUE4_PUFFER_FACTORY_SITE = FUN_080d1ac8 at 0x080d1b84
```
