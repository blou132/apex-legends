# Root preflight

Git started clean on `main` synchronized with `origin/main`. Exactly one
authorized PRA-LX1/C33/hi6250 target was present and no Samsung endpoint was
visible. Android was healthy with boot completed, 100 percent battery, and
28-29 C before flash. Read-only state showed flash lock `0`, verified boot
`ORANGE`, and Huawei `oem get-bootinfo` returned `unlocked`.

| Input | Size | SHA256 result |
| --- | ---: | --- |
| RAMDISK.img | 16777216 | `ED91177CF438CCDB256D6507203A42D784FD1B22FC941A25D71B67C153C97D57` |
| RECOVERY_RAMDISK.img | 33554432 | Phase16D exact match |
| KERNEL.img | 25165824 | Phase16D exact match |
| C33 firmware ZIP | 2540397881 | Phase16D exact match |

```text
GIT_PREFLIGHT_OK = YES
TARGET_IDENTITY_CONFIRMED = YES
PHONE_HEALTH_OK = YES
ADB_READY = YES
BOOTLOADER_UNLOCKED = YES
STOCK_RAMDISK_HASH_MATCH = YES
```
