# Magisk post-boot

After the patched boot, Magisk reported installed version `28.1 (28100)`,
`Ramdisk: Oui`, and exposed the `su` binary. It did not request a vbmeta,
recovery, factory-reset, or other additional setup operation.

```text
MAGISK_POSTBOOT_DETECTED = YES
MAGISK_INSTALLED_VERSION = 28.1_28100
MAGISK_ADDITIONAL_SETUP_REQUIRED = NO
MAGISK_ADDITIONAL_SETUP_COMPLETED = NOT_REQUIRED
SU_BINARY_AVAILABLE = YES
```
