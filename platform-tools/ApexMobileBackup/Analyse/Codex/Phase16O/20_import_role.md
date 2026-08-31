# CreateDolphin import role

The branch at `0x05a314cc` genuinely targets the `CreateDolphin` PLT entry,
but import identity is not evidence of execution. The callsite has no semantic
entry path and its return is unused by the actual next callee.

This site cannot establish runtime factory ownership, Init dispatch, or the
real Dolphin bootstrap acquisition path.

```text
CREATEDOLPHIN_RESULT_SEMANTICALLY_USED = NO
CREATEDOLPHIN_IMPORT_ROLE = UNREACHABLE_STATIC_REFERENCE
```
