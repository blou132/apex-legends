# Evidence table

| Claim | Evidence | Result |
| --- | --- | --- |
| Exact stock input | Fresh local SHA256 | Exact Phase16D match |
| Official Magisk | GitHub release API, manifest, signature | v28.1/28100 validated |
| Same-device patch | Magisk UI log | `All done` |
| Patched image integrity | Phone and PC size/hash | Match; differs from stock |
| Unlocked pre-flash | Huawei `oem get-bootinfo` | Unlocked |
| RAMDISK-only flash | Sanitized Fastboot log | `ramdisk`, OKAY, exit 0 |
| Patched Android boot | Android/MTP and ADB properties | Healthy exact C33 boot |
| Root | `su -c id` | uid 0 |
| SELinux unchanged | `su -c getenforce` | Enforcing |
| Root proc read | `/proc/1/maps` first line | Readable |
| Persistence | One normal reboot and repeated checks | Root persists |
| Apex absent | Package-manager query | Not installed |

Raw logs, APK, images, and identifiers are not tracked.
