# Registration lifetime

The bounded ordering is:

1. `GameUpdateMgr` class metadata is available before lookup.
2. Entry creation, source-registry insertion, and owner assignment occur at
   unknown earlier points.
3. `SkipAppUpdate` retrieves and class-validates the entry.
4. It loads the non-null owner from entry `+0x38`.
5. It validates owner `+0x1f0` and `+0x44`, then calls CheckUpdate.

```text
GAMEUPDATEMGR_REGISTRATION_LIFETIME_ORDER = CLASS_METADATA_BEFORE_LOOKUP; REGISTRATION_AND_OWNER_ASSIGNMENT_UNKNOWN; ENTRY_LOOKUP_BEFORE_OWNER_CONSUMPTION; OWNER_VALIDATION_BEFORE_CHECKUPDATE
```
