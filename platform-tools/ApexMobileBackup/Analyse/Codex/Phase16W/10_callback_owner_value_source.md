# Callback owner value source

Because no non-null writer is proven, there is no eligible assigned register
to backward-slice.

```text
CALLBACK_OWNER_ASSIGNED_VALUE_SOURCE = UNKNOWN
CALLBACK_OWNER_EQUALS_DOLPHINUPDATER_THIS = UNKNOWN
```

The owner methods reached through known callbacks remain semantically
consistent with DolphinUpdater, but semantic consistency is not an assignment
proof.
