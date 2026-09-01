# Owner initializer

No function satisfies the required owner-initializer criteria. In particular,
there is no proven function that both initializes the callback table and
establishes callback `+0x08`, followed by multiple known `DolphinUpdater`
field stores.

`FUN_0a2a403c` is retained as a callback-table initializer only. It is not
promoted to a constructor.

```text
DOLPHINUPDATER_INITIALIZER = UNKNOWN
DOLPHINUPDATER_INITIALIZER_CONFIDENCE = NONE
```
