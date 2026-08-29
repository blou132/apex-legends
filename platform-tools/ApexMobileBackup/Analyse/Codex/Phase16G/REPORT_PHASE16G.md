# Phase16G - controlled Magisk root of PRA-LX1

Date: 2026-08-29

## Executive result

The unlocked stock PRA-LX1/C33 passed identity, health, bootloader, input-hash,
and rollback checks. Official Magisk v28.1/28100 was downloaded only from the
topjohnwu GitHub release, matched its release asset size, passed APK signature
verification, and was installed without an alternate version.

The exact C33 stock RAMDISK was transferred with matching phone, source, and
round-trip SHA256. Magisk patched that image on the same phone and reported
`All done`. The pulled 16777216-byte image matched its phone-side hash, differed
from stock, and remained gitignored.

One Fastboot operation wrote only `ramdisk`; it returned `OKAY` and exit code
zero. Android booted normally. Magisk reported installed version 28.1/28100 and
provided `uid=0(root)`. SELinux remained `Enforcing`, root could read
`/proc/1/maps`, and all results persisted after one controlled reboot. The
final phone state remained healthy at 100 percent battery and 30 C.

Apex remains uninstalled and unlaunched. No Samsung access, rollback, vbmeta,
recovery, kernel, system/vendor, module, Zygisk, hook, trace, or process attach
occurred.

## Final result

```text
TARGET_IDENTITY_CONFIRMED = YES
BOOTLOADER_UNLOCKED = YES

STOCK_RAMDISK_HASH_MATCH = YES
ROLLBACK_MATERIAL_READY = YES

MAGISK_VERSION = 28.1
MAGISK_VERSION_CODE = 28100
MAGISK_APK_SHA256 = 8BFD3346B3DA5814F82EFF6F1B1B5FEDD0AD585F39A25709B23EB54AAC45691D

MAGISK_APP_INSTALLED = YES
MAGISK_APP_RAMDISK_STATUS = YES

STOCK_RAMDISK_TRANSFER_VALID = YES
MAGISK_PATCH_SUCCESS = YES

PATCHED_RAMDISK_SHA256 = 21E15B385E6BED84F9A8128CB908556D53B7980E5DE645A53D8D7838B7C8EE77
PATCHED_IMAGE_SANITY = PASS

VBMETA_MODIFIED = NO
RECOVERY_RAMDISK_MODIFIED = NO

MAGISK_FLASH_READY = YES
PATCHED_RAMDISK_FLASH_EXECUTED = YES
PATCHED_RAMDISK_FLASH_SUCCESS = YES

FIRST_PATCHED_BOOT_RESULT = SUCCESS

ROLLBACK_EXECUTED = NO
ROLLBACK_STOCK_RAMDISK_SUCCESS = NOT_APPLICABLE
ROLLBACK_ANDROID_BOOT_SUCCESS = NOT_APPLICABLE

MAGISK_POSTBOOT_DETECTED = YES
MAGISK_ADDITIONAL_SETUP_COMPLETED = NOT_REQUIRED

SU_BINARY_AVAILABLE = YES
ROOT_UID_CONFIRMED = YES
SELINUX_STATE = Enforcing
ROOT_PROC_ACCESS = YES

ROOT_PERSISTS_AFTER_REBOOT = YES

ROOT_PROC_MAPS_CAPABILITY = YES
ROOT_NATIVE_TRACE_PRECONDITION = PROBABLE
ROOT_CALLBACK_TRACE_PRECONDITION = PROBABLE

ANDROID_BOOT_SUCCESS = YES
DEVICE_HEALTH_OK = YES

APEX_INSTALLED = NO
APEX_LAUNCHED = NO

CURRENT_BLOCKER = NONE_PHASE16G_COMPLETE
NEXT_STEP = SEPARATE_AUTHORIZED_ROOT_TRACE_CAPABILITY_PHASE

FINAL_GATE = A MAGISK_ROOT_PERSISTENT_AND_DEVICE_HEALTHY
COMMIT = Root PRA-LX1 with exact stock ramdisk Phase16G
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Privacy boundary

No serial, IMEI, Android ID, MAC, account data, token, unlock code, raw USB
instance ID, APK, stock/patched image, raw UI capture, or raw command log is
committed.
