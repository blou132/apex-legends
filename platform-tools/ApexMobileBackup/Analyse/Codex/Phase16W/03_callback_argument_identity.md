# Callback argument identity

At Ghidra `0x00545904`, Init null-tests entry `X5`. On the valid path,
Ghidra `0x00545980` stores that unchanged value to `GCloudDolphinImp+0x10`.
The provider's bounded secondary callback interface later reloads this field
and dispatches client callback slots.

The callback parameter is therefore unambiguous:

```text
GCLOUDDOLPHIN_CALLBACK_ARGUMENT_REGISTER = X5
GCLOUDDOLPHIN_CALLBACK_ARGUMENT_CONFIDENCE = CONFIRMED
PROVIDER_CALLBACK_STORAGE = GCLOUDDOLPHINIMP_PLUS_0x10
```
