# Client callback vtable

Phase15W proves that `GCloudPufferImp::FUN_00503024` invokes slot `+0x10` of
the downstream callback stored at `GCloudPufferImp+0x18`. Phase15X does not
resolve the object or vtable supplied to that field, so the slot target cannot
be selected without a global vtable/class search.

No thunk or implementation is promoted.

```text
CLIENT_CALLBACK_SLOT = +0x10
CLIENT_CALLBACK_SLOT_10_TARGET = UNKNOWN
CLIENT_CALLBACK_SLOT_10_ADDRESS = UNKNOWN
CLIENT_SECONDARY_CALLBACK_FIELD = UNKNOWN
CLIENT_SECONDARY_CALLBACK_TARGET = UNKNOWN
```
