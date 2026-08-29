# Post-unlock stock boot

After the accepted unlock and factory reset, the phone returned as a normal
Huawei USB/MTP device and remained stable for more than 30 seconds. Android
displayed the initial language selector. The owner selected French, verified
touch input, and completed the bounded first-boot check without restoring Apex
or signing into a personal account.

After the read-only bootloader validation, `fastboot reboot` returned `OKAY`.
The phone again returned to normal Huawei USB/MTP instead of remaining in
Fastboot or entering recovery.

```text
ANDROID_BOOT_SUCCESS = YES
POST_COMMAND_BUILD = PRA-LX1_8.0.0.364_C33
DISPLAY_OK = YES
TOUCH_OK = YES
USB_MTP_OK = YES
APEX_INSTALLED_AFTER_WIPE = NO_FACTORY_RESET_NO_RESTORE
APEX_LAUNCHED = NO
```
