# Callback object base

`[DolphinUpdater+0x1f8]` is passed directly as the provider callback pointer.
The same field is nullable, receives a virtual slot `+0x08` cleanup during
Shutdown, and is cleared after cleanup. This upgrades its prior generic
related-interface role.

```text
CALLBACK_OBJECT_BASE_SOURCE = [DOLPHINUPDATER_PLUS_0X1F8]
OWNER_PLUS_0X1F8_CALLBACK_RELATION = CALLBACK_POINTER_CONFIRMED
```

This proves the interface role of the pointer, but not the concrete table
installed on its target instance.
