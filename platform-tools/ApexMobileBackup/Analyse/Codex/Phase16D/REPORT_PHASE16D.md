# Phase16D - exact PRA-LX1 C33 recovery material validation

Date: 2026-08-28

## Executive result

The exact `PRA-LX1 8.0.0.364(C33)` / Altice `all` service package identified
as `05014GCW` is now archived locally under gitignored storage. Its immutable
2,540,397,881-byte ZIP has SHA256
`CC39E24033EEF61F8C477822A66C011ED731AE56C1CBBB822B1D1598150E4C10`
and exactly matches the MD5 published by Android File Host.

Source provenance remains `REPUTABLE_ARCHIVE`, not official: no live Huawei
distribution endpoint was found. Identity confidence is nevertheless `HIGH`.
The embedded release notes and upgrade guide name the exact external build,
the extracted CUST filesystem proves `altice/all` and `Cust-033000`, both
nested update ZIPs contain Huawei AndroidTeam-signed updater metadata, and all
43 `.APP` records pass their per-block checksum tables.

Exact stock `RAMDISK.img`, `KERNEL.img`, and the recovery ramdisk/vendor/vbmeta
set were extracted and hashed. The ramdisk was not patched. The package's own
guide documents the paired main/Altice microSD recovery layout and normal plus
forcible upgrade modes, but warns that upgrade erases user data. Individual
fastboot restoration remains probable rather than confirmed on this device.

Current PotatoNV upstream explicitly supports Kirin 655 and lists the PRA
family with `Kirin 65x (A)`. The latest official GitHub release was archived
and hash-pinned but not executed. Three independent exact-family visual
references agree on the accessible PRA mainboard contact, while an exact-device
repair case supplies functional USB COM corroboration. This meets the planning
standard at medium confidence; the actual board revision remains unverified
until a separately authorized physical inspection.

The preserved Apex APK and both OBBs were rehashed on the PC and exactly match
their known values. The Phase16C evidence blockers are therefore resolved and
the preparation gate moves to `GO`. This is a documentation gate only, not
authorization to open the phone, run PotatoNV, unlock, wipe, flash, or root.

## Final result

```text
EXACT_FIRMWARE_FOUND = YES
EXACT_FIRMWARE_SOURCE_CLASS = REPUTABLE_ARCHIVE
EXACT_FIRMWARE_IDENTITY_CONFIDENCE = HIGH
FIRMWARE_SOURCE = https://androidfilehost.com/?fid=8889791610682891130
FIRMWARE_FILENAME = PRA-LX1_8.0.0.364_C33_05014GCW_original.zip
FIRMWARE_SIZE = 2540397881

EXACT_MODEL_MATCH = YES
EXACT_BUILD_MATCH = YES
EXACT_CUST_MATCH = YES

FIRMWARE_SHA256 = CC39E24033EEF61F8C477822A66C011ED731AE56C1CBBB822B1D1598150E4C10

STOCK_RAMDISK_FOUND = YES
STOCK_RAMDISK_SHA256 = ED91177CF438CCDB256D6507203A42D784FD1B22FC941A25D71B67C153C97D57

STOCK_RECOVERY_RAMDISK_FOUND = YES
STOCK_RECOVERY_RAMDISK_SHA256 = 9146C2BE6CC2C77BE08827F9839412C06DB6F85C979C9B5113D1CC3640D46D8A

INSTALLED_TO_STOCK_MATCH_CONFIDENCE = HIGH

POTATONV_KIRIN655_SUPPORT = YES_UPSTREAM_DOCUMENTED
POTATONV_PRA_SUPPORT = YES_UPSTREAM_TESTED

TESTPOINT_REFERENCE_FOUND = YES
TESTPOINT_REFERENCE_CONFIDENCE = VERIFIED_ENOUGH_MEDIUM
TESTPOINT_CROSSCHECK_COUNT = 3

MAGISK_EXACT_STOCK_INPUT_READY = YES

APEX_REINSTALL_READY_AFTER_WIPE = YES

RECOVERY_PLAN_READY = YES
RECOVERY_CONFIDENCE = MEDIUM

DATA_WIPE_RISK = HIGH
SOFT_BRICK_RISK = MEDIUM
HARD_BRICK_RISK = HIGH
PHYSICAL_DISASSEMBLY_RISK = HIGH

ROOT_PREPARATION_GATE = GO

PHONE_OPENED = NO
POTATONV_EXECUTED = NO
BOOTLOADER_UNLOCKED = NO
ROOT_INSTALLED = NO
DEVICE_WIPED = NO

NEXT_STEP = SEPARATE_EXPLICIT_AUTHORIZATION_FOR_PHYSICAL_PREFLIGHT_AND_CONTROLLED_UNLOCK_PHASE

FINAL_GATE = A ROOT_PREPARATION_GO_PC_EVIDENCE_COMPLETE
COMMIT = Validate PRA-LX1 C33 recovery material Phase16D
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

No firmware, image, extraction tool, photo, PotatoNV binary, APK, OBB, raw
phone output, identifier, token, or temporary signed URL is committed.
