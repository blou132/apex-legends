# Apex OBB inventory

The public OBB directory contains exactly the two expected expansion files.

| File | Phone bytes | Preserved PC bytes | Device-local timestamp | Result |
| --- | ---: | ---: | --- | --- |
| `main.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1942013346 | 1942013346 | `2026-08-13 17:37` | exact name and size match |
| `patch.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1837582506 | 1837582506 | `2026-08-13 17:38` | exact name and size match |

Both preserved PC files were present and their current sizes matched the phone.
Because there was no discrepancy, no multi-gigabyte phone hash was performed.
Neither OBB was opened for writing, replaced, moved, or deleted.

```text
APEX_OBB_STATE = INTACT_EXACT_NAME_AND_SIZE_MATCH
PHONE_HASH_REPEATED = NO_NOT_JUSTIFIED
```
