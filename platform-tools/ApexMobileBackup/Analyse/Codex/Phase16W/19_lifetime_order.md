# Lifetime order

The bounded order is partial:

```text
DOLPHINUPDATER_LIFETIME_ORDER = OWNER_EXISTS; CALLBACK_POINTER_PLUS_0X1F8_AND_INTERFACE_PLUS_0X1F0_ARE_NON_NULL_BEFORE_INIT; PROVIDER_PERSISTS_CALLBACK; CHECKUPDATE_CONTINUES; ASSIGNMENT_ORDER_OPAQUE
```

Shutdown later cleans and clears `+0x1f8` and clears `+0x1f0`. No evidence
orders the callback owner binding relative to the `+0x1f0` acquisition.
