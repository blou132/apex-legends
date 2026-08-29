# RAMDISK flash

One Fastboot command wrote only the patched image to the `ramdisk` partition.
The sanitized trace contained `Sending 'ramdisk'`, `Writing 'ramdisk'`, `OKAY`,
and exit code zero.

No other partition was selected or modified.

```text
PATCHED_RAMDISK_FLASH_EXECUTED = YES
PATCHED_RAMDISK_FLASH_SUCCESS = YES
FASTBOOT_EXIT = 0
VBMETA_MODIFIED = NO
RECOVERY_RAMDISK_MODIFIED = NO
KERNEL_MODIFIED = NO
SYSTEM_MODIFIED = NO
VENDOR_MODIFIED = NO
```
