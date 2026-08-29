# Stock RAMDISK transfer

The exact 16777216-byte C33 `RAMDISK.img` was copied with ADB to:

`/sdcard/Download/PRA_C33_stock_RAMDISK.img`

The phone-side SHA256 matched the source. An ADB pull-back copy also matched
the exact source size and SHA256. MTP was not used for patch input or output.

```text
STOCK_RAMDISK_SHA256 = ED91177CF438CCDB256D6507203A42D784FD1B22FC941A25D71B67C153C97D57
STOCK_RAMDISK_TRANSFER_VALID = YES
REMOTE_HASH_MATCH = YES
ROUNDTRIP_HASH_MATCH = YES
```
