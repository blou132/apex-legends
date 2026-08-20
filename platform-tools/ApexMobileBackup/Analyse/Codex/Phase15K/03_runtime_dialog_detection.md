# Runtime dialog detection

Apex was not launched because the SystemUI overlay remained present at the
mandatory preflight check. Consequently there is no Phase15K post-resume time
origin, window polling sequence, or runtime dialog reproduction result.

Phase15J remains authoritative for the earlier observation that the Apex-owned
window count changed from two to three between +15 and +30 seconds.

```text
VALIDATION_CACHE_REUSED = NOT_TESTED_NO_APEX_LAUNCH
GAMEACTIVITY_RESUMED = NO
APEX_DIALOG_REPRODUCED = NO_NOT_LAUNCHED
APEX_DIALOG_APPEAR_TIME = NOT_APPLICABLE
```
