# Stock recovery plan

## Exact firmware evidence

Community firmware indexes list an exact package named for PRA-LX1/PRA-L11,
Altice `all`, EMUI 8, build `8.0.0.364(C33)`, package identifier `05014GCW`:

- [Android File Host listing](https://androidfilehost.com/?fid=8889791610682891130)
- [Android Firmware File index](https://www.androidfirmwarefile.com/huawei-p8-lite-2017-pra-lx1-firmware-flash-file-download-stock-rom/)

These are third-party listings, not Huawei provenance. No package was
downloaded, hashed, extracted, or matched to the running device. Therefore the
exact firmware is only probable and must not yet be treated as recovery-ready.

Huawei documents HiSuite system restoration for rooted or modified phones:
[Huawei system restoration guidance](https://consumer.huawei.com/uk/support/content/en-gb00406898/).
Applicability and server availability for this old exact C33 build were not
tested.

## Recovery material still required

Before any future destructive phase:

1. Obtain the exact `05014GCW` package from a provenance that can be audited.
2. Record package hashes and verify model, CUST, build, and rollback metadata.
3. Extract and hash at minimum `KERNEL.img`, `RAMDISK.img`, and
   `RECOVERY_RAMDISK.img` without modifying them.
4. Establish a tested restore route for stock `ramdisk` and
   `recovery_ramdisk` before patching either.
5. Separately verify whether HiSuite/eRecovery still serves the device.

## Failure recovery mapping

| Failure | Required recovery material | Current readiness |
| --- | --- | --- |
| Magisk/ramdisk bootloop | Exact stock `RAMDISK.img` | NOT LOCAL |
| Bad custom recovery | Exact stock `RECOVERY_RAMDISK.img` | NOT LOCAL |
| Kernel mismatch | Exact stock `KERNEL.img` | NOT LOCAL |
| Wider system failure | Exact full C33 package and verified HiSuite/eRecovery route | UNVERIFIED |

```text
EXACT_STOCK_FIRMWARE_AVAILABLE = PROBABLE_THIRD_PARTY_INDEXED_NOT_VERIFIED
STOCK_BOOT_OR_RAMDISK_AVAILABLE = NO_LOCAL_COPY
STOCK_RECOVERY_AVAILABLE = NO_LOCAL_COPY
ERECOVERY_AVAILABLE = PARTITIONS_PRESENT_SERVICE_UNTESTED
RECOVERY_CONFIDENCE = LOW
```
