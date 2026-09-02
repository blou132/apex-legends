# Callback non-null writer

No non-null assignment to `callback+0x08` is reachable from the concrete
callback argument, the known callback initializer, or the already proven
DolphinUpdater lifecycle functions.

```text
CALLBACK_OWNER_NON_NULL_WRITE_SITE = UNKNOWN
CALLBACK_OWNER_NON_NULL_WRITE_FUNCTION = UNKNOWN
CALLBACK_OWNER_NON_NULL_WRITE_REACHABLE = NO
```

The concrete callback pointer is already non-null when the readable Init
continuation loads owner `+0x1f8`; its construction and owner binding precede
that frontier.
