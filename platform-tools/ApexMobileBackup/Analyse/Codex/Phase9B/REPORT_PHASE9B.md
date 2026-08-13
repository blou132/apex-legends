# Phase9B - Validate existing x86_64 image for ARM64 translation

Date: 2026-08-13

## Executive result

The only installed image is an Android 16/API 36.1 Google Play x86_64 image, package revision 4. Its local metadata explicitly declares `arm64-v8a` as a translated ABI and enables `libndk_translation.so`. The image therefore supports ARM64 translation by design.

A fresh `ApexPhase9Lab` AVD was created without downloading any component. The normal launch was refused because no usable x86_64 hypervisor is installed. A constrained `-accel off` attempt remained alive but never exposed `emulator-5580` or completed Android boot within six minutes. The AVD was stopped, and no APK, OBB, client, network request, hook, or bypass followed.

```text
SYSTEM_IMAGE = system-images;android-36.1;google_apis_playstore;x86_64
API = 36.1 / Android 16
IMAGE_ABI = x86_64
ARM64_TRANSLATION_SUPPORTED = YES
AVD_CREATED = YES
APK_INSTALL_RESULT = NOT_ATTEMPTED_LAB_NOT_BOOTED
CLIENT_STARTED = NO
LIBUE4_LOADED = NO
FINAL_GATE = F SYSTEM_IMAGE_UNSUITABLE_UNKNOWN
EVENTSYSTEM_OBSERVED = NO
NEXT_STEP = ENABLE_A_SUPPORTED_WINDOWS_EMULATOR_HYPERVISOR_AND_RETRY_THE_EXISTING_AVD
```

## Why translation support is confirmed

The classification is based on multiple independent fields in the installed image:

- ABI lists include `x86_64,arm64-v8a`.
- `package.xml` contains translated ABI `arm64-v8a`.
- the native bridge is `libndk_translation.so`.
- ARM64 is mapped to x86_64 execution.
- native bridge execution is enabled.
- the image notice inventories ARM64 runners, linkers, libraries, and translation proxies.

This proves image capability, not successful execution of this particular APK.

## Why the APK was not installed

The test protocol required a boot-complete emulator followed by guest ABI and native-bridge verification. No ADB-visible emulator existed after the bounded software attempt. Installing into an unavailable target was neither possible nor informative.

The recovered base APK remains local-only, unchanged, and identified as:

| Size | SHA256 | ABI |
| ---: | --- | --- |
| 96,228,800 | `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0` | `arm64-v8a` only |

## Host runtime boundary

Firmware virtualization and second-level address translation are present, but no active Windows hypervisor was detected and the emulator acceleration check reports no Android Emulator hypervisor driver. Software TCG emitted missing AVX/F16C warnings and did not deliver a guest endpoint in the allowed interval.

The final gate is deliberately `SYSTEM_IMAGE_UNSUITABLE_UNKNOWN`, not a no-translation conclusion: the image supports translation, while the current host cannot run it sufficiently to validate the APK.

## Publication and safety

The original phone was not used. Raw emulator logs, AVD configuration/userdata, APK, OBB, PAK, native libraries, identifiers, and proprietary content remain local-only. Only cleaned image properties, hashes, gate results, and the next prerequisite are published.
