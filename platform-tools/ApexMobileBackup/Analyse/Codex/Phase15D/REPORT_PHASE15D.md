# Phase15D - isolated Apex translated runtime

Date: 2026-08-20

## Executive result

The mandatory preflight found no physical Android device. WHPX remained usable,
the unchanged `ApexPhase9Lab` AVD completed boot, and all three local artifact
identities matched exactly. Guest storage was sufficient. The exact ARM64 APK
installed without bypass flags, package metadata matched version
`1.3.672.546` / code `64003140`, and both exact OBB files were installed with
matching byte sizes.

Before first launch, the guest was isolated with airplane mode enabled, Wi-Fi
and mobile data disabled, no active default network, and no default route. Apex
was launched once. The same process and foreground activity remained present at
about +5, +20, and +60 seconds without a fatal exception, native signal, ABI
failure, or restart.

In the verified x86_64/native-bridge guest, Android's native loader explicitly
loaded Apex libraries from the app's ARM64 directory and reported
`libUE4.so` loaded successfully. This confirms Apex ARM64 translated runtime and
the library load. The one permitted `showmap` and `debuggerd -b` attempts were
denied by OS permissions, so no mapping, frame, load bias, or runtime address
model is available.

The first application request was an HTTP POST to `tdm.mgapex.com`; it failed at
DNS with no response. No new runtime witness identifies Lua, `ClientLaunch`,
`EventSystem`, `Login`, or `RequestAvatarServerList`. Apex was force-stopped,
the exact initial network state was restored, and the AVD shut down cleanly.
All raw output remains local-only.

## Required results

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES

ARTIFACT_IDENTITY = MATCH
GUEST_FREE_SPACE = SUFFICIENT
APK_INSTALL_RESULT = SUCCESS
PACKAGE_VERSION_NAME = 1.3.672.546
PACKAGE_VERSION_CODE = 64003140
PACKAGE_PRIMARY_CPU_ABI = arm64-v8a

MAIN_OBB_PRESENT = YES
PATCH_OBB_PRESENT = YES
OBB_INSTALL_RESULT = SUCCESS

NETWORK_ISOLATION_CONFIRMED = YES

APEX_LAUNCH_RESULT = SUCCESS_OFFLINE_STABLE_60S
PROCESS_ALIVE_5S = YES
PROCESS_ALIVE_20S = YES
PROCESS_ALIVE_60S = YES

ARM64_TRANSLATION_RUNTIME = CONFIRMED
SHOWMAP_APEX = NO_PERMISSION
DEBUGGERD_APEX = NO_PERMISSION

LIBUE4_LOADED = CONFIRMED
LIBUE4_LOAD_BIAS = UNKNOWN
RUNTIME_ADDRESS_MODEL_VALIDATED = NO

LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENTSYSTEM_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE

FINAL_GATE = B APEX_TRANSLATED_RUNTIME_CONFIRMED_LIBUE4_MAPPING_UNKNOWN
COMMIT = Validate isolated Apex translated runtime Phase15D
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence limit

`libUE4.so` loading is confirmed by a direct loader event, not by a process-map
read. Phase15D does not establish a mapping range, PT_LOAD correlation, load
bias, native PC, JNI runtime address, Lua path, backend response, or account
state.
