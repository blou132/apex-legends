# Phase9C - Secondary Huawei ARM64 runtime lab

Date: 2026-08-13

## Executive result

The secondary Huawei `PRA-LX1` is an Android 8.0/API 26 ARM64 device with sufficient free storage. Apex was absent, all three local artifact identities matched, and the unchanged base APK installed successfully. Both OBBs were copied and their exact remote sizes matched.

For the first no-hook run, Wi-Fi and mobile data were blocked only on the Huawei. Android started the package's `GameActivity`; its process and activity remained alive for more than 90 seconds without a fatal Java or native crash. The display remained black and the first actionable error was a hostname/backend resolution failure expected under the deliberate network block. The app was then stopped and the Huawei network state was restored exactly.

```text
HUAWEI_MODEL = PRA-LX1
ANDROID_VERSION = 8.0.0
SDK = 26
ARM64_SUPPORTED = YES
HUAWEI_LAB_COMPATIBLE = YES
APK_INSTALL_RESULT = APK_INSTALL_SUCCESS
OBB_INSTALLED = YES
CLIENT_STARTED = YES
LIBUE4_LOADED = NOT_CONFIRMED
FIRST_FAILURE = DNS_BACKEND_UNAVAILABLE_UNDER_NETWORK_BLOCK
EVENTSYSTEM_OBSERVED = NO
FINAL_GATE = B HUAWEI_CLIENT_STARTS_THEN_BACKEND_FAILURE
```

## Device and compatibility

The device reports `arm64-v8a` as its primary and only 64-bit ABI. Its SDK 26 satisfies the historical minimum SDK 23. Before installation, `/data` had 6,462,672 KiB free and `/sdcard` had 6,442,192 KiB free. The package was absent.

No Huawei serial or other stable identifier is included. The preserved Samsung was not targeted by any Phase9C command.

## Installation evidence

| Artifact | Local identity | Device result |
| --- | --- | --- |
| base APK | 96,228,800 bytes, SHA256 `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0` | streamed install `Success` |
| main OBB | 1,942,013,346 bytes | exact remote size match |
| patch OBB | 1,837,582,506 bytes | exact remote size match |

The log did not name either OBB, so runtime OBB detection remains `NOT_CONFIRMED` despite correct file placement.

## Startup evidence

The process and foreground activity were repeatedly observed. The log contains UE4-tagged Java/startup messages and no ABI loader failure, fatal exception, or native fatal signal. However, `/proc/<pid>/maps` was not readable to the unprivileged shell, and `lsof` did not expose mapped shared libraries. `LIBUE4_LOADED` therefore remains `NOT_CONFIRMED`; UE4-tagged messages alone are not used as proof of a native mapping.

No useful Lua, ClientLaunch, EventSystem, provider, OpenRead, chunk, event-subscriber, parser, or URL-source metadata was observed. No runtime address or hook was used.

## Gate rationale

Gate `B HUAWEI_CLIENT_STARTS_THEN_BACKEND_FAILURE` is selected because Android successfully starts and retains the client process, while the first actionable failure is backend/DNS resolution under the intentional offline policy. This is not classified as native-load failure, Android incompatibility, insufficient resources, or anti-integrity blocking.

## Safety and publication

No real account, historical token, private Samsung state, root, patch, proxy, bypass, proprietary Lua, memory dump, raw logcat, screenshot, APK, OBB, PAK, serial, or device identifier is published. Raw evidence remains local-only. The Huawei application was stopped and its initial network state restored after the test.
