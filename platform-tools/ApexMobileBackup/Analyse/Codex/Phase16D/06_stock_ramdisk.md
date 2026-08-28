# Exact stock RAMDISK

The main `UPDATE.APP` contains a separate partition named exactly `RAMDISK`.
It was extracted without transformation and its per-block package checksums
pass.

```text
STOCK_RAMDISK_FOUND = YES
STOCK_RAMDISK_SIZE = 16777216
STOCK_RAMDISK_SHA256 = ED91177CF438CCDB256D6507203A42D784FD1B22FC941A25D71B67C153C97D57
MAGISK_EXACT_STOCK_INPUT_READY = YES
```

7-Zip identifies the image as an Android image with an `ANDROID!` header and a
gzip payload at offset 2048. It was not patched, copied to the phone, or used
by Magisk.

Phase16C identified the separate `ramdisk` partition as the probable EMUI 8
Magisk target. Current Magisk guidance still requires an unlocked bootloader,
an image from the device's own firmware, and patching on the target device.
The future write path remains untested and is not authorized here.
