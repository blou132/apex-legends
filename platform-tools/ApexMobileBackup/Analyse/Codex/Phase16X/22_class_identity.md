# Class identity

Phase16X confirms the selected wrapper class as
`/Script/PureClient.GameUpdateMgr`. Its `+0x38` owner is ABI-compatible with
the DolphinUpdater object consumed by CheckUpdate.

The missing non-null assignment prevents proving that this is the same exact
owner flow as the three `UDolphinUpdater` argument-0 callers or the callback
lifecycle. The DolphinUpdater identity therefore remains probable.

```text
DOLPHINUPDATER_CLASS_IDENTITY = PROBABLE
SELECTED_ENTRY_SEMANTIC_ROLE = MANAGER_WRAPPER
```
