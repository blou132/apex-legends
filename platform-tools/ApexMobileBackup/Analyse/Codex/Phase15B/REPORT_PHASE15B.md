# Phase15B - WHPX post-reboot validation

Date: 2026-08-20

## Executive result

Windows Hypervisor Platform is now enabled and Windows reports an active
hypervisor. Android Emulator 36.4.9.0 returns acceleration exit code `0` and
explicitly confirms that `WHPX(10.0.26200) is installed and usable`.

`ApexPhase9Lab` remains the only listed AVD. It still targets the Android 36.1
Google Play x86_64 image whose metadata declares `arm64-v8a` translation through
`libndk_translation.so`, ARM64-to-x86_64 ISA mapping, and native-bridge
execution.

The requested optional-feature cmdlet required elevation in the current shell,
so the enabled state was confirmed through the read-only WMI optional-feature
provider. Because acceleration succeeded, the conditional elevated BCD audit
was neither necessary nor performed.

No Windows setting, BCD value, BIOS option, driver, service, or AVD was changed.
No emulator, Apex client, Huawei, or preserved Samsung was used.

## Required results

```text
WHPX_FEATURE_STATE = CONFIRMED ENABLED
ACCEL_CHECK_RESULT = CONFIRMED WHPX(10.0.26200) INSTALLED_AND_USABLE; EXIT_CODE_0
HYPERVISOR_LAUNCH_TYPE = NOT_AUDITED_ACCELERATION_CONFIRMED
APEX_PHASE9_LAB_PRESENT = CONFIRMED YES
AVD_SYSTEM_IMAGE = system-images;android-36.1;google_apis_playstore;x86_64
ARM64_TRANSLATION_EXPECTED = CONFIRMED YES
FINAL_GATE = CONFIRMED A WHPX_ACCELERATION_CONFIRMED
```

## Stop boundary

Phase15B validates host acceleration and static AVD configuration only. It does
not prove guest boot, ARM64 execution, APK compatibility, or client behavior.
