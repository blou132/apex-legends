# Recovery plan

This is a readiness map, not an execution procedure. Every path still requires
a separate authorization and a fresh state check.

| Failure | Exact local material | Future mode/path | Classification |
| --- | --- | --- | --- |
| Bad Magisk ramdisk, fastboot still available | `RAMDISK.img` | Restore partition `ramdisk` from unlocked fastboot, subject to Huawei FBLOCK policy | PROBABLE |
| Bad recovery image, fastboot still available | `RECOVERY_RAMDISK.img`, recovery vendor/vbmeta set | Restore the exact recovery partition set from unlocked fastboot | PROBABLE |
| Kernel mismatch | `KERNEL.img` | Restore partition `kernel` from unlocked fastboot | PROBABLE |
| Bootloop or wider partition damage | Untouched main and Altice `dload` packages | Exact package's documented microSD normal/forcible upgrade route | DOCUMENTED_FOR_PLATFORM |
| Rooted system still accepted by Huawei service tooling | Full exact package plus PC | HiSuite system restoration | DOCUMENTED_FOR_PLATFORM |
| eRecovery remains bootable and service still serves a package | Embedded eRecovery partitions | Huawei eRecovery download-and-restore route | DOCUMENTED_FOR_PLATFORM |

The embedded exact C33 guideline documents both normal and forcible microSD
upgrade, including the paired main and Altice packages. It also warns that the
upgrade erases user data. Neither dload, HiSuite, nor eRecovery was tested in
Phase16D.

Generic fastboot syntax is not evidence that Huawei will accept a write. The
individual partition restoration paths remain `PROBABLE`, not
`CONFIRMED_FOR_DEVICE`, until a future unlocked-device audit proves the
available commands and FBLOCK state.

```text
RECOVERY_PLAN_READY = YES
RECOVERY_CONFIDENCE = MEDIUM
```
