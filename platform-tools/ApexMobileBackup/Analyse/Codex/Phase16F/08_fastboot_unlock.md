# Fastboot unlock

The temporary PotatoNV fastboot interface was observed by Windows but was not
usable during the bounded operation because its driver was not yet bound. A
normal Huawei fastboot endpoint was not entered afterward, and no unlock code
existed.

The required destructive readiness gate therefore remained closed. The
command `fastboot oem unlock <private code>` was not executed.

```text
DEVICE_FASTBOOT_VISIBLE = NO_USABLE_ENDPOINT
DESTRUCTIVE_UNLOCK_READY = NO
BOOTLOADER_UNLOCK_COMMAND_EXECUTED = NO
```
