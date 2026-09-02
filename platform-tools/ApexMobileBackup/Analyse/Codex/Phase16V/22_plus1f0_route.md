# Plus 0x1f0 route

Every CheckUpdate caller already has an owner object before invoking the
method. `SkipAppUpdate` explicitly proves this by loading the owner from
`selected_entry+0x38` and testing owner `+0x1f0` before the call.

This is a reader/gate, not a non-null `+0x1f0` acquisition writer. It therefore
does not create a new specific acquisition region under the authorized scope.

```text
DOLPHINUPDATER_OBJECT_PREEXISTS_CHECKUPDATE = YES
OBJECT_CREATION_VS_INTERFACE_ACQUISITION = UNKNOWN
NEW_PLUS1F0_PROVENANCE_ROUTE = NO
REAL_DOLPHIN_INIT_DISPATCH_PROVEN = YES
DOLPHIN_INIT_CALL_SITE = ELF_0x05a2f0ac
```
