# AVD setup

## Created lab

```text
AVD_CREATED = YES
AVD_NAME = ApexPhase9Lab
```

No `avdmanager` binary is installed, so a minimal AVD configuration was created locally against the existing image. It uses:

- image `system-images;android-36.1;google_apis_playstore;x86_64`
- fresh data partition with 12 GiB capacity
- no snapshot boot
- no camera or microphone input
- no Google or application account
- fixed emulator port `5580`

The AVD configuration and generated user data are outside Git.

## Startup attempts

The standard accelerated launch stopped before Android because x86_64 emulation requires hardware acceleration and the Android Emulator hypervisor driver is absent.

A software fallback was tested without Apex:

- `-accel off`
- fresh AVD data
- 2 GiB RAM and two virtual cores
- no window, audio, boot animation, snapshot, metrics, or hardware GPU

The emulator process remained alive, but `emulator-5580` never became available and `sys.boot_completed` could not be read after a six-minute deadline. The process was then stopped cleanly.

## Host acceleration evidence

- firmware virtualization: enabled
- second-level address translation: available
- active Windows hypervisor: not detected
- emulator acceleration check: no Android Emulator hypervisor driver
- software TCG warnings: missing requested AVX/F16C features
