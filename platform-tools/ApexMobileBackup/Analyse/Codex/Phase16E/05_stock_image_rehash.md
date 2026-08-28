# Stock material rehash

All exact local C33 recovery artifacts requested for Phase16E remain
accessible and match their Phase16D SHA256 values.

| Artifact | Size | SHA256 | Match |
| --- | ---: | --- | --- |
| Firmware ZIP | 2,540,397,881 | `CC39E24033EEF61F8C477822A66C011ED731AE56C1CBBB822B1D1598150E4C10` | YES |
| `KERNEL.img` | 25,165,824 | `6993D273EFCD35D91AC3E0029C9FA4E0BC42999F6BE835B35A92C8F2C670006C` | YES |
| `RAMDISK.img` | 16,777,216 | `ED91177CF438CCDB256D6507203A42D784FD1B22FC941A25D71B67C153C97D57` | YES |
| `RECOVERY_RAMDISK.img` | 33,554,432 | `9146C2BE6CC2C77BE08827F9839412C06DB6F85C979C9B5113D1CC3640D46D8A` | YES |

The exact main and Altice `dload` packages are also accessible offline. Their
future use remains a recovery action requiring separate authorization, and
individual fastboot restoration remains `PROBABLE`, not confirmed for this
locked device.

Future recovery routing remains the Phase16D hierarchy:

- Exact paired main/Altice microSD `dload` recovery is documented by the
  package itself, including normal and forcible upgrade modes; it wipes data.
- HiSuite and eRecovery are documented platform routes, but future service and
  package availability are not proven.
- Individual stock kernel/ramdisk/recovery restoration is only `PROBABLE` and
  must not be attempted unless a later audit proves the unlocked fastboot and
  FBLOCK write policy.

```text
FIRMWARE_HASH_MATCH = YES
KERNEL_HASH_MATCH = YES
RAMDISK_HASH_MATCH = YES
RECOVERY_RAMDISK_HASH_MATCH = YES
RECOVERY_PACKAGE_READY = YES_LOCAL_EXACT_C33_MEDIUM_CONFIDENCE
```
