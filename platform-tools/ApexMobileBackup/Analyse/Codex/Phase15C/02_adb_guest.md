# ADB guest state

No emulator endpoint existed at preflight and the AVD was not started.

```text
EMULATOR_ADB_VISIBLE = NO
EMULATOR_SERIAL_PRESENT = NO
ANDROID_BOOT_COMPLETED = NOT_ATTEMPTED_AVD_NOT_STARTED
ANDROID_VERSION = UNKNOWN_NOT_BOOTED
ANDROID_SDK = UNKNOWN_NOT_BOOTED
GUEST_BUILD_TYPE = UNKNOWN_NOT_BOOTED
GUEST_DEBUGGABLE = UNKNOWN_NOT_BOOTED
```

The only ADB action was the host-level endpoint inventory. No command selected
or queried the physical device.
