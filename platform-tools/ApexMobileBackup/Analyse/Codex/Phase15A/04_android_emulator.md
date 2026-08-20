# Android Emulator

The existing SDK emulator was queried only with the three authorized commands.
No virtual device was started.

```text
EMULATOR_VERSION = 36.4.9.0
EMULATOR_BUILD = 14788078
ACCEL_CHECK_EXIT_CODE = 6
ACCEL_CHECK_RESULT = ANDROID_EMULATOR_HYPERVISOR_DRIVER_NOT_INSTALLED
APEX_PHASE9_LAB_PRESENT = YES
```

`-accel-check` currently finds no usable acceleration provider. This result is
consistent with WHPX being disabled and AEHD being absent. It does not indicate
that the CPU or installed system image is unsuitable.

`-list-avds` returned only `ApexPhase9Lab`. Post-audit process inspection
confirmed that no emulator or QEMU process was running.
