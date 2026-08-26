# CreatePuffer wrapper

`FUN_080d1ac8` starts at Ghidra `0x080d1ac8`. Its incoming `x0` is an Unreal
object used to resolve/check an interface implementation. The success path
restores the wrapper frame and executes:

```text
0x080d1b84  b 0x0a4c1730
```

`0x0a4c1730` is the `libUE4` import thunk for `CreatePuffer`. Because this is a
tail branch after frame restoration, the facade returned by `CreatePuffer` in
`x0` is returned directly to the wrapper's unresolved caller. There is no
wrapper-local store, field assignment, smart pointer, or second call after the
factory operation.

The non-factory paths return from `FUN_080d1ac8` without reaching
`CreatePuffer`; they do not provide facade-object dataflow.

```text
CREATEPUFFER_WRAPPER = FUN_080d1ac8 at 0x080d1ac8
CREATEPUFFER_CALL_SITE = 0x080d1b84
CREATEPUFFER_RETURN_USAGE = RETURNED_DIRECTLY_BY_TAIL_CALL
```
