# Phase15S - Existing PRA-LX1 Apex lab audit

Date: 2026-08-25

## Executive result

Exactly one physical Android target was present and it is the expected Huawei
PRA-LX1 running Android 8/API 26 on ARM64. The installed Apex package matches
version `1.3.672.546` code `64003140`; it is a non-debuggable production build.
The two OBB names and byte sizes exactly match the authoritative preserved PC
copies, so no expensive phone-side hash was justified.

The Mali-T830 reports OpenGL ES 3.2 and exposes both
`GL_EXT_color_buffer_half_float` and `GL_EXT_color_buffer_float`. Standard ADB,
logcat, UIAutomator, screenshot, and dumpsys capabilities are available without
root. Apex private data and run-as access are unavailable as expected.

Storage is the blocking gate. Initial free space was approximately
`226-246 MiB`; final read-only observations fluctuated to `413-433 MiB`, still
far below the mandatory `2 GiB`. Download and media folders are empty, Apex is
the only third-party package, its OBBs are protected, and the remaining large
consumers are preinstalled system components. No deletion was safe or useful
under the authorized rules, so no cleanup was performed.

## Required result

```text
PHONE_MODEL = PRA-LX1
ANDROID_VERSION = 8.0.0
SDK = 26
PRIMARY_ABI = arm64-v8a

APEX_PACKAGE_PRESENT = YES
APEX_VERSION_NAME = 1.3.672.546
APEX_VERSION_CODE = 64003140
APEX_OBB_STATE = INTACT_EXACT_NAME_AND_SIZE_MATCH

FREE_SPACE_INITIAL = /data ~246 MiB; shared ~226 MiB
FREE_SPACE_FINAL = /data 443032 KiB (432.6 MiB); shared 422552 KiB (412.6 MiB)
PHONE_STORAGE_READY = NO

GL_VENDOR = ARM
GL_RENDERER = Mali-T830
GL_VERSION = OpenGL ES 3.2 v1.r20p0-01rel0.9a7fca3f7dd712a473937294a8ae24b1
GLES31_AVAILABLE = YES
FLOAT_RT_CAPABILITY = YES_BOTH_EXTENSIONS_PRESENT

LOGCAT_READABLE = YES
UIAUTOMATOR_AVAILABLE = YES
DUMPSYS_AVAILABLE = YES
PROC_MAPS_APEX_ACCESSIBLE = NO_CURRENT_PROCESS; PRIOR_SAME_DEVICE_SHELL_DENIAL
PRIVATE_DATA_APEX_ACCESSIBLE = NO

ROOT_PRESENT = NO
BOOTLOADER_UNLOCK_STATE = LOCKED

PHONE_LAB_READY_FOR_RUNTIME = NO

APEX_UNINSTALLED = NO
APEX_DATA_CLEARED = NO
FACTORY_RESET_PERFORMED = NO

SENSITIVE_IDENTIFIERS_REDACTED = YES

FINAL_GATE = B SAFE_FREE_SPACE_TARGET_NOT_REACHED
COMMIT = Audit existing PRA-LX1 Apex lab Phase15S
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

Phase15S proves package/OBB integrity, platform graphics capability, and the
current unprivileged diagnostic surface. It does not test Apex startup,
client-specific graphics initialization, backend behavior, private data, or
process maps. Runtime work remains blocked by the explicit storage minimum.
