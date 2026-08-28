# Risk reassessment

## Improvements from Phase16C

- The exact C33 archive is now local, immutable, and hash-pinned.
- Package model/build/CUST identity is independently supported by internal
  metadata and the extracted CUST filesystem.
- Every `.APP` payload checksum passes.
- Exact stock ramdisk, kernel, and recovery material are local and hashed.
- A medium-confidence exact-family testpoint reference is cross-checked.
- The Apex APK and OBB preservation was rehashed successfully.

## Remaining risks

- Bootloader unlocking is expected to erase userdata.
- The individual fastboot restoration commands and Huawei FBLOCK behavior are
  not confirmed on this exact locked device.
- HiSuite/eRecovery availability for this obsolete C33 service build was not
  tested.
- The physical board revision is unknown until disassembly.
- Testpoint work can damage the rear cover, fingerprint cable, board, or USB
  path; exact stock software cannot repair physical damage.
- The package came from a reputable third-party archive, not a live Huawei
  distribution endpoint.

```text
DATA_WIPE_RISK = HIGH
SOFT_BRICK_RISK = MEDIUM
HARD_BRICK_RISK = HIGH
PHYSICAL_DISASSEMBLY_RISK = HIGH
RECOVERY_CONFIDENCE = MEDIUM
```

Soft-brick readiness improves from Phase16C because exact recovery bytes now
exist. Hard-brick and physical risks remain high because no device-side restore
route or physical operation was tested.
