# Phase16E - PRA-LX1 physical and host unlock preflight

Date: 2026-08-28

## Executive result

The non-destructive PC and normal-ADB preflight is complete. The authorized
PRA-LX1 was the sole Android endpoint and remained in normal boot. Three stable
reads reported 97 percent battery, USB charging, 25.0 C, exact model/build, and
the expected locked/green/enforcing baseline. The active data path uses a
Windows root hub and AMD host controller without an external hub.

The exact Huawei testpoint driver archive linked by PotatoNV upstream is now
stored under ignored local inputs. Its 10,227,102-byte content has SHA256
`A5C9A980228A3505792A97C9AD445A88582A8417578453D13F4E0115EA241BD3`;
its MD5 exactly matches the independent Android File Host listing. The x64
`HUAWEI USB COM 1.0` INF is version `2.0.7.1`. Windows SDK kernel-policy
verification succeeds for its Microsoft WHCP catalog and for the INF and SYS
through that catalog. The package was not installed.

The official pinned PotatoNV archive and executable, exact C33 firmware,
kernel, ramdisk, and recovery ramdisk all match their Phase16D SHA256 values.
The exact offline recovery package is accessible with medium recovery
confidence. Three local PRA visual references were loaded and converted into
a landmark checklist.

The phone was not opened, so actual board revision and testpoint geometry are
`NOT_INSPECTED`. Personal-data absence also cannot be proven non-invasively;
the Apex APK/OBBs are preserved, but the owner must repeat a final backup review
before any future wipe. The resulting gate is therefore
`READY_PENDING_BOARD_VISUAL_CONFIRMATION`, not authorization to unlock.

## Final result

```text
WIPE_ACCEPTED_AS_EXPECTED_CONSEQUENCE = YES_FOR_FUTURE_PHASE_PLANNING
BATTERY_READY = YES_97_PERCENT_CHARGING_25C

VCOM_DRIVER_READY = YES_VALIDATED_OFFLINE_NOT_INSTALLED
VCOM_DRIVER_SIGNATURE_STATE = MICROSOFT_WHCP_CATALOG_VALID_INF_AND_SYS_CATALOG_VERIFIED

POTATONV_BINARY_HASH_MATCH = YES

FIRMWARE_HASH_MATCH = YES
KERNEL_HASH_MATCH = YES
RAMDISK_HASH_MATCH = YES
RECOVERY_RAMDISK_HASH_MATCH = YES

USB_HOST_STABLE = YES_THREE_ADB_READS_DIRECT_ROOT_HUB

TESTPOINT_REFERENCE_READY = YES_THREE_LOCAL_REFERENCES_LOADED
TESTPOINT_BOARD_MATCH = NOT_INSPECTED

RECOVERY_PACKAGE_READY = YES_LOCAL_EXACT_C33_MEDIUM_CONFIDENCE

ABORT_CONDITIONS_DEFINED = YES

PHONE_OPENED = NO
POTATONV_EXECUTED = NO
BOOTLOADER_UNLOCKED = NO
ROOT_INSTALLED = NO
DEVICE_WIPED = NO

PHYSICAL_UNLOCK_PREFLIGHT = READY_PENDING_BOARD_VISUAL_CONFIRMATION

NEXT_STEP = SEPARATE_EXPLICIT_AUTHORIZATION_FOR_PHASE16F_DESTRUCTIVE_UNLOCK

FINAL_GATE = B READY_PENDING_BOARD_VISUAL_CONFIRMATION
COMMIT = Prepare PRA-LX1 controlled unlock preflight Phase16E
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

No firmware, driver, image, PotatoNV binary, APK, OBB, local reference image,
raw phone output, device identifier, token, or temporary URL is committed.
