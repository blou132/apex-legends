# Init callback argument provenance

`FUN_0050323c` stores incoming `x2` at `GCloudPufferImp+0x18`, as established
in Phase15W. Phase15X cannot trace `x2` backward on the client side because no
Init call site is reachable from the `CreatePuffer` return.

No stack object, heap allocation, owner field, global singleton, static object,
factory return, or local constructor is promoted. Selecting one would require
an unanchored scan outside the permitted object-flow chain.

```text
CLIENT_CALLBACK_PROVENANCE = UNKNOWN
CLIENT_CALLBACK_SOURCE_FUNCTION = UNKNOWN
CLIENT_CALLBACK_SOURCE_FIELD = UNKNOWN
CLIENT_CALLBACK_DESTINATION_FIELD = GCloudPufferImp+0x18
```
