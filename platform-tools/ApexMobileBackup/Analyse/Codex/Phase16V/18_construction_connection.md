# Construction connection

`FUN_0a2a403c` initializes the callback object itself; it does not establish the
non-null DolphinUpdater owner. The owner allocation and assignment remain
unseen, so no connection to the known primary/secondary DolphinUpdater group
can be promoted.

```text
DOLPHINUPDATER_CONSTRUCTION_CONNECTION = UNKNOWN
```
