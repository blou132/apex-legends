# Callback success anchor

`FUN_080f87d0` contains the exact
`DolphinCallback::OnDolphinFirstExtractSuccess` identifier and source basename
`DolphinUpdaterCallback.cpp`. It loads `[this + 0x08]`, returns when null, and
otherwise calls `FUN_080f6f04` with the stored owner state.

```text
DOLPHIN_CALLBACK_SUCCESS_FUNCTION = FUN_080f87d0
DOLPHIN_CALLBACK_OWNER_FIELD_OFFSET = 0x08
DOLPHIN_CALLBACK_OWNER_LOAD_CONFIRMED = YES
```
