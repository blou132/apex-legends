# Exact stock recovery material

The exact C33 main package includes a complete recovery set:

| Artifact | Bytes | SHA256 |
| --- | ---: | --- |
| `RECOVERY_RAMDISK.img` | 33554432 | `9146C2BE6CC2C77BE08827F9839412C06DB6F85C979C9B5113D1CC3640D46D8A` |
| `RECOVERY_VENDOR.img` | 16777216 | `B852D5C12FCAD03E227942E8702E9A59ED9BBD674B200C35387763E133728412` |
| `RECOVERY_VBMETA.img` | 7104 | `64377CDEAB599DA6FB75009457DFC9A6618841A98CDB460B1715623D93A8B05F` |

The package also contains matching eRecovery kernel, ramdisk, vendor, and
vbmeta records. Those records passed package checksum verification but were
not needed as the primary root recovery artifacts.

```text
STOCK_RECOVERY_RAMDISK_FOUND = YES
STOCK_RECOVERY_RAMDISK_SIZE = 33554432
STOCK_RECOVERY_RAMDISK_SHA256 = 9146C2BE6CC2C77BE08827F9839412C06DB6F85C979C9B5113D1CC3640D46D8A
```

No recovery image was flashed or tested on the phone.
