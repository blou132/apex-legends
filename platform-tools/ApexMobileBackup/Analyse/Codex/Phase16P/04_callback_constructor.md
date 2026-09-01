# Callback initialization

`FUN_0a2a403c` writes table address `0x0af55170` at object `+0x00` and zero at
`+0x08`. No direct branch to this function or static object instance proves a
conventional constructor callsite. It is therefore retained as a proven
initializer/reset slot, not promoted to a constructor.

```text
DOLPHIN_CALLBACK_CONSTRUCTOR = UNKNOWN
DOLPHIN_CALLBACK_INITIALIZER = FUN_0a2a403c
DOLPHIN_CALLBACK_OBJECT_SIZE = MINIMUM_0x10_EXACT_SIZE_UNKNOWN
OWNER_FIELD_INITIAL_VALUE = NULL
```
