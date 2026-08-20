# Emulator acceleration check

The installed Android Emulator was queried without starting an AVD.

```text
EMULATOR_VERSION = 36.4.9.0
ACCEL_CHECK_EXIT_CODE = 0
ACCEL_CHECK_RESULT = WHPX(10.0.26200) IS INSTALLED AND USABLE
```

This is direct confirmation that Android Emulator can use Windows Hypervisor
Platform after the reboot. Phase15A's acceleration code `6` and missing-driver
gate are superseded.
