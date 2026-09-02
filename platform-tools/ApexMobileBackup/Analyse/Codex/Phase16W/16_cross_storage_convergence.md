# Cross-storage convergence

Both fields are consumed as a DolphinUpdater-like owner:

- `DolphinCallback+0x08` feeds callback continuations.
- selected `GameUpdateMgr` entry `+0x38` feeds CheckUpdate.

Neither non-null assignment producer is available. Consumption semantics alone
cannot establish pointer equality or a common producer.

```text
CALLBACK_GAMEUPDATEMGR_OWNER_CONVERGENCE = UNKNOWN
```
