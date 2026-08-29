# Phase16F-2 - single controlled PRA-LX1 bootrom retry

Date: 2026-08-29

## Executive result

The one authorized physical retry reproduced the exact Phase16F success.
`VID_12D1/PID_3609` enumerated once with the signed Huawei VCOM driver, status
OK, and problem code zero. The pinned PotatoNV executable used only
`Kirin 65x (A)` with FBLOCK disabling off. It uploaded `hisi65x_a`, passed the
previous D00D timeout, connected through the installed `oem77.inf` WinUSB
path, read the exact PRA-LX1/C33 information, completed its documented NV/code
operation, and rebooted. The generated code remains private and gitignored.

Stock Android returned healthy. Exactly one stock-fastboot endpoint was then
entered and reported `locked`. The first authorized `fastboot oem unlock`
command was rejected because OEM unlock/FRP permission was not enabled. No
confirmation or wipe began, and the phase stopped safely.

The owner subsequently enabled `OEM unlocking` through the normal Android UI.
Read-only ADB confirmed `sys.oem_unlock_allowed=1`. The owner then explicitly
authorized one new destructive fastboot command without another testpoint or
PotatoNV run. After all target and health checks were repeated, the stock
unlock prompt was confirmed on-device. Fastboot announced the factory reset,
returned `OKAY`, and exited with code zero.

The phone completed the wipe and returned to the initial Android language
selector. Display, touch, and USB/MTP worked, and no personal account or Apex
restore was used. A normal Volume-down entry then exposed exactly one fastboot
endpoint with no Samsung present. The Huawei-specific read-only command `oem
get-bootinfo` returned `unlocked`, `OKAY`, and exit code zero. Generic
`getvar unlocked/secure` queries are unsupported by this bootloader, so a
specific verified-boot color is not claimed. A final normal fastboot reboot
returned the phone to stock Android USB/MTP.

The device is healthy, wiped, stock, unlocked, and unrooted. No further
physical entry, PotatoNV operation, unlock command, root work, or Apex work is
needed in this phase.

## Final result

```text
TARGET_IDENTITY_CONFIRMED = YES
INPUT_HASHES_VALID = YES

PHONE_OPENED = YES
TESTPOINT_BOARD_MATCH = CONFIRMED

PHYSICAL_ATTEMPT_COUNT = 1
PHYSICAL_ATTEMPT_LIMIT = ONE

BOOTROM_ENUMERATION = YES_VID_12D1_PID_3609
BOOTROM_LIVE_DRIVER_BOUND = YES_OEM14_STATUS_OK_PROBLEM_0

POTATONV_PROFILE_CONFIRMED = YES_KIRIN_65X_A
FBLOCK_CHANGE_REQUESTED = NO
POTATONV_EXECUTED = YES_SINGLE_BOUNDED_OPERATION

TEMP_FASTBOOT_ENUMERATED = YES_PROVEN_BY_EXACT_POTATONV_D00D_PATH
TEMP_FASTBOOT_DRIVER_BOUND = YES_OEM77_WINUSB_FUNCTIONAL
TEMP_FASTBOOT_LIBUSB_VISIBLE = YES

POTATONV_PASSED_PREVIOUS_BLOCKER = YES
DEVICE_INFORMATION_STAGE_REACHED = YES

UNLOCK_CODE_OBTAINED = YES
UNLOCK_CODE_PUBLISHED = NO

POTATONV_COMPLETED = YES

STOCK_FASTBOOT_VISIBLE = YES_EXACTLY_ONE_ENDPOINT
DESTRUCTIVE_UNLOCK_READY = YES_AFTER_SEPARATE_REAUTHORIZATION

BOOTLOADER_UNLOCK_COMMAND_EXECUTED = YES_SECOND_COMMAND_ACCEPTED
BOOTLOADER_UNLOCK_COMMAND_COUNT = 2_FIRST_REJECTED_SECOND_AUTHORIZED
DEVICE_WIPED = YES

BOOTLOADER_UNLOCKED = YES_OEM_GET_BOOTINFO
VERIFIED_BOOT_STATE = UNLOCKED_NOT_DIRECTLY_READ
ANDROID_BOOT_SUCCESS = YES
DEVICE_HEALTH_OK = YES

RECOVERY_MATERIAL_STILL_READY = YES

MAGISK_INSTALLED = NO
ROOT_INSTALLED = NO
APEX_INSTALLED_AFTER_WIPE = NO
APEX_LAUNCHED = NO

CURRENT_BLOCKER = NONE_PHASE_COMPLETE

FINAL_GATE = A BOOTLOADER_UNLOCKED_AND_STOCK_ANDROID_HEALTHY
COMMIT = Retry PRA-LX1 bootloader unlock Phase16F-2
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Privacy boundary

No unlock code, serial, IMEI, Android ID, Board ID, MAC, account data, token,
raw USB instance ID, raw photo, raw log, firmware, driver, PotatoNV binary,
APK, or OBB is committed.
