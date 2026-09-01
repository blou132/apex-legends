# Interface lifetime model

`CheckUpdate` consumes an already non-null persistent field, and `Shutdown`
later invokes cleanup then clears it. The initial assignment is not visible,
so this evidence cannot distinguish eager owner construction, lazy acquisition,
or external injection before `CheckUpdate`.

```text
DOLPHIN_INTERFACE_LIFETIME_MODEL = UNKNOWN
```
