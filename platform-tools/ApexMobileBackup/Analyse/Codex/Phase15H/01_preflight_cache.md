# Preflight and validation cache

The mandatory initial ADB inventory contained no endpoint, so no physical
Android device was present. WHPX acceleration was usable and the only selected
AVD was the existing `ApexPhase9Lab`. It booted without snapshot load/save and
without wipe.

The installed package matched version `1.3.672.546`, code `64003140`. The main
and patch OBBs had the expected names and exact byte sizes:

| Artifact | Bytes | Result |
| --- | ---: | --- |
| main OBB | 1,942,013,346 | `MATCH` |
| patch OBB | 1,837,582,506 | `MATCH` |

The targeted `cacheFile.txt` was present and readable. It contained exactly the
two expected OBB rows, and both recorded last-modified values matched the
installed OBB values. No cache or artifact was modified.

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES
PACKAGE_STATE_VALID = YES
OBB_STATE_VALID = YES
VALIDATION_CACHE_PRESENT = YES
VALIDATION_CACHE_CONTENT_VALID = YES
```
