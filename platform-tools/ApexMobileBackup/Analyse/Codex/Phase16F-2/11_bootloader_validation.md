# Bootloader validation

After the stable first boot, the owner entered stock Fastboot/Rescue mode using
the normal Volume-down cable sequence. No testpoint was used. The host found
exactly one fastboot endpoint and no Samsung endpoint. The read-only Huawei
command `fastboot oem get-bootinfo` returned `(bootloader) unlocked`, `OKAY`,
and exit code zero.

This bootloader rejects generic `getvar unlocked` and `getvar secure` queries
as `Command not allowed`; those unsupported queries do not contradict the
successful Huawei-specific boot-info result. Verified-boot color was therefore
not directly read after the wipe and is not inferred as a specific color.

```text
BOOTLOADER_UNLOCKED = YES_OEM_GET_BOOTINFO
BOOTLOADER_VALIDATION_ENDPOINT_COUNT = 1
SAMSUNG_ENDPOINT_COUNT = 0
VERIFIED_BOOT_STATE = UNLOCKED_NOT_DIRECTLY_READ
GENERIC_GETVAR_UNLOCKED = UNSUPPORTED_COMMAND_NOT_ALLOWED
```
