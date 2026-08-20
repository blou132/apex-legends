# Preflight

The first successful Android inventory was `adb devices -l`; it returned no
endpoint. No physical phone was present before the AVD started.

Android Emulator acceleration returned code `0` and reported WHPX usable. The
existing AVD list contained `ApexPhase9Lab`. It booted without snapshot load,
snapshot save, or data wipe and reached `sys.boot_completed=1`.

Before launch, the installed package matched:

| Field | Observed | Expected | Result |
| --- | --- | --- | --- |
| Version name | `1.3.672.546` | `1.3.672.546` | match |
| Version code | `64003140` | `64003140` | match |
| Main OBB bytes | `1,942,013,346` | `1,942,013,346` | match |
| Patch OBB bytes | `1,837,582,506` | `1,837,582,506` | match |

The files retained their exact expected names in the production Android OBB
directory. They were not rehashed in the guest.

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES
PACKAGE_STATE_VALID = YES
OBB_STATE_VALID = YES
```
