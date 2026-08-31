# Post-extraction callback relation

`FUN_080f87d0` is anchored as
`DolphinCallback::OnDolphinFirstExtractSuccess`. After optional logging, it
loads `x0` from `[this + 0x08]`. A null pointer returns immediately; a non-null
pointer calls `FUN_080f6f04`, which in turn invokes the target path-bundle
function.

This proves a success-callback and owner-present condition for the upper chain.
It does not make the callback the Dolphin bootstrap owner and does not bypass
the noreturn barrier inside the target.

```text
POST_EXTRACTION_CALLBACK_TO_TARGET_CONDITION = SUCCESS_CALLBACK_AND_OWNER_POINTER_NON_NULL
```
