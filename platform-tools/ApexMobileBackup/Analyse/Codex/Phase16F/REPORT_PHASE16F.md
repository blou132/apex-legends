# Phase16F report

## Executive result

The authorized PRA-LX1 was opened and its actual motherboard matched the three
archived references. The exact validated point successfully produced the
expected Huawei bootrom endpoint. The signed Huawei VCOM driver bound correctly
and the hash-pinned PotatoNV v2.2.1 release was started with the upstream PRA
profile `Kirin 65x (A)`, FBLOCK unchanged.

PotatoNV verified and uploaded its RAM bootloader, but Windows initially lacked
the driver for the resulting `Fastboot2.0` endpoint. PotatoNV timed out before
reading device information or writing NV data. The exact matching signed
fastboot driver from the already-validated Huawei archive was then installed,
but subsequent physical entries produced only normal Huawei USB mode. The
phase stopped under the no-trial-and-error rule.

No unlock code was generated, no bootloader unlock command was executed, and
no userdata wipe occurred. The PRA-LX1 returned to stock Android with normal
Huawei USB and ADB functionality before it was unplugged. It remains locked,
stock, and unrooted.

## Final result

```text
TARGET_IDENTITY_CONFIRMED = YES
INPUT_HASHES_VALID = YES

PHONE_OPENED = YES
TESTPOINT_BOARD_MATCH = CONFIRMED

TESTPOINT_CONTACT_ATTEMPTED = YES
EXPECTED_BOOTROM_ENUMERATION = YES

VCOM_DRIVER_INSTALLED = YES
VCOM_DEVICE_DETECTED = YES

POTATONV_PROFILE_CONFIRMED = KIRIN_65X_A
FBLOCK_CHANGE_REQUESTED = NO
POTATONV_EXECUTED = YES_BOUNDED_ATTEMPT_NOT_COMPLETED

UNLOCK_CODE_OBTAINED = NO
UNLOCK_CODE_PUBLISHED = NO

DEVICE_FASTBOOT_VISIBLE = NO_USABLE_ENDPOINT
DESTRUCTIVE_UNLOCK_READY = NO

BOOTLOADER_UNLOCK_COMMAND_EXECUTED = NO
DEVICE_WIPED = NO

BOOTLOADER_UNLOCKED = NO
VERIFIED_BOOT_STATE = LOCKED_GREEN_BASELINE_UNCHANGED
ANDROID_BOOT_SUCCESS = YES

DEVICE_HEALTH_OK = YES_AT_CONTROLLED_STOP
RECOVERY_MATERIAL_STILL_READY = YES

MAGISK_INSTALLED = NO
ROOT_INSTALLED = NO
APEX_LAUNCHED = NO

CURRENT_BLOCKER = BOOTROM_NOT_REPRODUCED_AFTER_TEMP_FASTBOOT_DRIVER_INSTALL
NEXT_STEP = SEPARATE_RETRY_DECISION_BEFORE_ANY_PHASE16G_WORK
SEPARATE_PHASE16G_MAGISK_ROOT_PREPARATION = NOT_READY

FINAL_GATE = D BOOTROM_DETECTED_POTATONV_NOT_COMPLETED
COMMIT = Unlock PRA-LX1 bootloader Phase16F
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Privacy and evidence boundary

No unlock code, serial, IMEI, Android ID, MAC address, board ID, token, account
identifier, raw photo, raw runtime log, driver package, firmware, PotatoNV
binary, APK, or OBB is committed. Private runtime artifacts remain under
ignored local storage.
