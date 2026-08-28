# Bootloader and host-tool state

Normal read-only ADB properties were collected without entering fastboot.
Unique identifiers were excluded.

```text
MODEL = PRA-LX1
BOARD = hi6250
ANDROID = 8.0.0
BUILD = PRA-LX1 8.0.0.364(C33)
FLASH_LOCKED = 1
VBMETA_DEVICE_STATE = locked
VERIFIED_BOOT_STATE = GREEN
VERITY_MODE = enforcing
OEM_UNLOCK_SUPPORTED = 1
OEM_UNLOCK_ALLOWED = 0
FRP_STATE = NOT_DIRECTLY_OBSERVABLE_FROM_NORMAL_ADB
```

`dumpsys persistent_data_block`, the FRP partition property, and candidate boot
FRP properties did not expose a reliable FRP state. No conclusion is inferred
from absence of output. FBLOCK also remains unknown because no fastboot session
was entered.

Host tools are present:

```text
ADB_VERSION = Android Debug Bridge version 1.0.41
FASTBOOT_VERSION = 36.0.2-14143358
FASTBOOT_COMMAND_USED_ON_PHONE = NO
```

The observed state is the expected locked baseline. Any different model,
build, lock state, verified-boot state, or OEM/FRP result at the start of a
future phase is a hard stop pending reassessment.
