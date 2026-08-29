# Magisk patch

Before flash, the Magisk home screen reported `Version installee: Non
disponible`, `Ramdisk: Oui`, and app version `28.1 (28100)`. The owner used only
`Select and Patch a File` on the exact transferred stock RAMDISK. Recovery Mode
was not enabled.

The sanitized patch log reached:

```text
Copying image to cache
Checking ramdisk status
Patching ramdisk
magisk_patched-28100_<random>.img
All done
```

```text
MAGISK_APP_RAMDISK_STATUS = YES
MAGISK_APP_INSTALLED_STATUS_BEFORE_FLASH = NOT_INSTALLED
MAGISK_PATCH_SUCCESS = YES
MAGISK_PATCHED_FILENAME = magisk_patched-28100_<random>.img
```
