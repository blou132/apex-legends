# GameUpdateMgr assignment

No non-null assignment to the selected entry's `+0x38` field is reachable from
the exact lookup and consumer path.

```text
GAMEUPDATEMGR_OWNER_ASSIGNMENT_SITE = UNKNOWN
GAMEUPDATEMGR_OWNER_ASSIGNMENT_FUNCTION = UNKNOWN
GAMEUPDATEMGR_OWNER_VALUE_SOURCE = UNKNOWN
```

The field value is a confirmed CheckUpdate owner at consumption time, but its
registration producer remains unresolved.
