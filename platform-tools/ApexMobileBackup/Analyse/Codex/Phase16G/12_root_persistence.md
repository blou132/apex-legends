# Root persistence

One controlled normal reboot was performed. Android returned as normal
Huawei USB/MTP. EMUI did not immediately expose its ADB interface; after the
owner re-enabled USB debugging and reconnected the cable, the expected ADB
interface appeared with status OK and authorization intact.

Post-reboot checks returned boot completed, Magisk `28.1/28100`,
`uid=0(root)`, SELinux `Enforcing`, and readable `/proc/1/maps`.

```text
ROOT_PERSISTS_AFTER_REBOOT = YES
ANDROID_HEALTHY_AFTER_REBOOT = YES
MAGISK_PERSISTS_AFTER_REBOOT = YES
SELINUX_AFTER_REBOOT = Enforcing
```
