# Storage convergence

Five normalized caller origins are retained: three distinct function argument
sites, one selected-entry field, and one callback owner field. Two of these are
actual field-backed locations.

The OnNotice and first-extract callback paths converge exactly on
`DolphinCallback+0x08`. The three argument-based CheckUpdate paths do not expose
their producer storage, and the fourth uses a different field
`selected_entry+0x38`. Shutdown has no external caller beyond its thunk.

```text
OWNER_STORAGE_CANDIDATE_COUNT = 5_NORMALIZED_ORIGINS_2_FIELD_BACKED
CHECKUPDATE_SHUTDOWN_STORAGE_CONVERGENCE = UNKNOWN
ONNOTICE_STORAGE_CONVERGENCE = SAME_EXACT_STORAGE
FIRSTEXTRACT_STORAGE_CONVERGENCE = SAME_EXACT_STORAGE
```
