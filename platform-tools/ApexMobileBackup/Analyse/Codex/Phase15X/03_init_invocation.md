# Init invocation

Phase15W established that the concrete facade is
`GCloud::GCloudPufferImp`. Its primary interface vtable is Ghidra
`0x009784c0`; slot `+0x10` resolves inside `libgcloud.so` to
`FUN_0050323c`, the facade Init implementation.

Phase15X does not find a `libUE4` call on the same returned pointer. Because
`FUN_080d1ac8` has no statically resolved caller and its `x0` return is consumed
through unresolved indirect dispatch, no Init invocation can be connected by
return-value dataflow.

The known callee ABI is:

```text
x0 = GCloudPufferImp this
x1 = Puffer Init configuration
x2 = downstream client callback
```

This identifies the callback register at the callee boundary but not the value
supplied by `libUE4`.

```text
PUFFER_INIT_CALLER_FUNCTION = UNKNOWN
PUFFER_INIT_CALL_SITE = UNKNOWN
PUFFER_INIT_VTABLE_SLOT = +0x10 (semantic known; client invocation unresolved)
PUFFER_INIT_TARGET_SEMANTIC = GCloudPufferImp::Init / FUN_0050323c
PUFFER_INIT_CLIENT_CALLBACK_ARGUMENT_REGISTER = x2
PUFFER_INIT_CLIENT_CALLBACK_VALUE = UNKNOWN
```
