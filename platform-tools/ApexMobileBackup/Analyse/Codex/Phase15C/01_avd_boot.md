# AVD boot

## Preflight

```text
PHYSICAL_DEVICE_PRESENT = CONFIRMED NO
PREEXISTING_ADB_ENDPOINT = CONFIRMED NO
WHPX_ACCELERATION = CONFIRMED WHPX(10.0.26200) INSTALLED_AND_USABLE
WHPX_ACCEL_CHECK_EXIT_CODE = 0
AVD_NAME = ApexPhase9Lab
SYSTEM_IMAGE = system-images;android-36.1;google_apis_playstore;x86_64
```

## Boot result

The existing AVD started with normal WHPX acceleration and no snapshot load,
snapshot save, or data wipe. An emulator ADB endpoint appeared without any
physical endpoint. Android then reported `sys.boot_completed=1` and stopped the
boot animation within the five-minute guest-boot limit.

```text
AVD_STARTED = CONFIRMED YES
EMULATOR_ADB_VISIBLE = CONFIRMED YES
ANDROID_BOOT_COMPLETED = CONFIRMED YES
BOOT_COMPLETION_AFTER_ADB_SECONDS = 201.8
```
