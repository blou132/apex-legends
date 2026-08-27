# Phase16C - PRA-LX1 root feasibility and recovery audit

Date: 2026-08-27

## Executive result

The authorized lab phone is exactly a Huawei PRA-LX1 on the `hi6250` platform,
identified as Kirin 655, running Android 8.0.0 / EMUI 8 build
`PRA-LX1 8.0.0.364(C33)`. Normal-boot properties independently confirm that the
bootloader and vbmeta device state are locked, verified boot is green, and
verity is enforcing.

PotatoNV upstream supports Kirin 655 and lists the PRA model family as tested
with `Kirin 65x (A)`. A same-model EMUI 8 community success exists, but for a
different C432 build. No exact C33 PotatoNV report was found. PotatoNV requires
physical testpoint access and disassembly; no sufficiently reliable exact board
reference has yet been validated.

The EMUI 8 device exposes separate `kernel`, `ramdisk`, and
`recovery_ramdisk` partitions. `RAMDISK.img` is therefore the supported Magisk
candidate, not a generic `boot.img`, but no exact stock C33 image is present.
Third-party indexes list the exact `05014GCW` firmware package; no official
provenance, local copy, hash, extraction, or restoration test exists.

The preserved Apex APK and both OBBs remain available on the PC with their
known hashes and documented install procedure. This is enough to reinstall the
public client material after a wipe, but not to preserve private app state.
Android documents userdata erasure during a bootloader state transition.

The gate is `NO_GO`: compatibility and Apex restore pass, but exact stock
recovery material and a trustworthy exact testpoint reference do not.

## Required result

```text
MODEL = PRA-LX1
SOC = HISILICON_KIRIN_655_HI6250
ANDROID = 8.0.0
EMUI = EMOTIONUI_8.0.0
FULL_BUILD = PRA-LX1 8.0.0.364(C33)
CUST_REGION = C33_ALTICE_ALL

BOOTLOADER_LOCKED = YES
OEM_UNLOCK_TOGGLE_AVAILABLE = UNKNOWN
OEM_UNLOCK_TOGGLE_ENABLED = NO_BY_SYS_PROPERTY

PRA_LX1_SUPPORTED_BY_POTATONV = YES_UPSTREAM_TESTED_PRA
KIRIN655_SUPPORTED_BY_POTATONV = YES_UPSTREAM_DOCUMENTED
TESTPOINT_REQUIRED = YES

EXACT_BUILD_POTATONV_EVIDENCE = NONE_FOUND
SAME_MODEL_EMUI8_EVIDENCE = COMMUNITY_CONFIRMED_DIFFERENT_CUST

BOOTLOADER_UNLOCK_EXPECTED_TO_WIPE_USERDATA = YES

APEX_POST_WIPE_REINSTALL_READY = YES

EXACT_STOCK_FIRMWARE_AVAILABLE = PROBABLE_THIRD_PARTY_INDEXED_NOT_VERIFIED
STOCK_BOOT_OR_RAMDISK_AVAILABLE = NO_LOCAL_COPY
STOCK_RECOVERY_AVAILABLE = NO_LOCAL_COPY
ERECOVERY_AVAILABLE = PARTITIONS_PRESENT_SERVICE_UNTESTED

MAGISK_PATCH_TARGET = RAMDISK.img
MAGISK_METHOD_CONFIDENCE = MEDIUM

ROOT_WOULD_ENABLE_PROC_MAPS = YES
ROOT_WOULD_ENABLE_NATIVE_STACKS = PROBABLE_WITH_ADDITIONAL_TOOLING
ROOT_WOULD_ENABLE_CALLBACK_TRACE = PROBABLE_WITH_ADDITIONAL_TOOLING

DATA_WIPE_RISK = HIGH
SOFT_BRICK_RISK = HIGH
HARD_BRICK_RISK = HIGH
PHYSICAL_DISASSEMBLY_RISK = HIGH
RECOVERY_CONFIDENCE = LOW

ROOT_PREPARATION_GATE = NO_GO

APEX_LAUNCHED = NO
BOOTLOADER_UNLOCKED = NO
ROOT_INSTALLED = NO
DEVICE_WIPED = NO

NEXT_STEP = PC_ONLY_EXACT_C33_FIRMWARE_RECOVERY_AND_TESTPOINT_VALIDATION

FINAL_GATE = D ROOT_PREPARATION_NO_GO
COMMIT = Audit PRA-LX1 root feasibility Phase16C
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

Only cleaned metadata is committed. No ADB serial, IMEI, Android ID, MAC,
account identifier, token, raw device output, private application data, APK,
OBB, firmware image, or patched image is published.
