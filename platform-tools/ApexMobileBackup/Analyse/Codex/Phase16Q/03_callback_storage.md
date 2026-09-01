# Callback storage

With no exact direct initializer caller, no caller-side `X0` object placement
or surrounding owner store can be recovered. The callback's own `+0x08` owner
field remains proven by Phase16P, but its source and the callback object's
placement inside or outside `DolphinUpdater` are not statically attributable.

```text
DOLPHIN_CALLBACK_STORAGE_KIND = UNKNOWN
DOLPHIN_CALLBACK_OWNER_OFFSET = UNKNOWN
CALLBACK_PLUS_0X08_SOURCE = UNKNOWN_NO_REACHABLE_INITIALIZER_CALLER
CALLBACK_OWNER_IS_ENCLOSING_DOLPHINUPDATER = UNKNOWN
```
