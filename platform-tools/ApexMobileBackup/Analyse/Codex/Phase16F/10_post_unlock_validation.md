# Post-operation validation

There was no unlock to validate. After the PotatoNV timeout, the PRA-LX1
returned to its stock Android USB composition and normal ADB became available.
The model remained PRA-LX1. Since PotatoNV never reached its NV write path and
no OEM unlock command ran, the locked baseline was not changed.

The phone was later unplugged by the operator, so no further live checks were
performed.

```text
BOOTLOADER_UNLOCKED = NO
VERIFIED_BOOT_STATE = LOCKED_GREEN_BASELINE_UNCHANGED
ANDROID_BOOT_SUCCESS = YES
MODEL_MATCH = YES
BUILD_MATCH_OR_EXPECTED_RESET_STATE = STOCK_BUILD_NO_RESET
```
