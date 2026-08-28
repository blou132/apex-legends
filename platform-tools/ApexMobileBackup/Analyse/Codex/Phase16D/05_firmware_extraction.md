# Firmware extraction

Extraction was performed offline against local copies. No updater executable
was run.

## Tooling

- `huawei-playground` `update-extractor.py`, commit
  `b6e2202bb252b5f6eac2a99e8fc979bef2f19f90`, script SHA256
  `151D6E2582B9FDD7C89B7D74273EB2766498278FE15E875CC4E67E4059019952`.
- 7-Zip 26.00 for ZIP, DOCX, and sparse ext4 inspection.
- A local-only C# verifier implementing the documented `huextract` CRC16
  algorithm. Reference source commit:
  `f4f0d8a0c2d4fccece7f2e7eecc5280af94633a7`.

## Main package inventory

The main package has 34 records. Recovery-relevant entries are:

| Partition | Bytes |
| --- | ---: |
| `KERNEL` | 25165824 |
| `RAMDISK` | 16777216 |
| `RECOVERY_RAMDISK` | 33554432 |
| `RECOVERY_VENDOR` | 16777216 |
| `RECOVERY_VBMETA` | 7104 |
| `ERECOVERY_KERNEL` | 25165824 |
| `ERECOVERY_RAMDISK` | 33554432 |
| `ERECOVERY_VENDOR` | 16777216 |
| `SYSTEM` | 2870597820 |
| `VENDOR` | 590009596 |
| `CUST` | 9007396 |

The separate customization package has 9 records, including a 139984248-byte
`CUST` image and 5977316-byte `VERSION` image.

## Checksum result

The verifier recomputed the Huawei CRC16 for every 4096-byte payload block.
All 34 main records and all 9 CUST records matched their embedded checksum
tables. This verifies extraction integrity; it is not a substitute for an
independently trusted Huawei signature chain.

```text
MAIN_RECORDS_CHECKED = 34
MAIN_RECORDS_VALID = 34
CUST_RECORDS_CHECKED = 9
CUST_RECORDS_VALID = 9
```
