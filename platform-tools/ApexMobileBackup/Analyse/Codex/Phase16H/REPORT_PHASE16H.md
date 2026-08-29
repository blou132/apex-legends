# Phase16H - PRA-LX1 cleanup and root trace capability

Date: 2026-08-29

## Executive result

The rooted PRA-LX1 already exceeded the five-GiB future Apex headroom target.
Minimal hygiene removed only two verified Phase16G RAMDISK transfer copies
whose authoritative PC copies and hashes were confirmed. No package, cache,
personal media, system component, Magisk data, or recovery material was
removed. Immediate measured recovery was 33603584 bytes.

Official Frida 17.17.0 Android ARM64 was verified against the upstream GitHub
asset digest and tested from an isolated host environment. USB discovery and
process enumeration succeeded. Root also read the disposable ping process's
maps and identified its executable, linker, and libc mapping.

The Frida attach did not succeed: the client reported that the remote server
connection closed before script injection. No module enumeration, Frida memory
read, interceptor attachment, or native call observation occurred. The phase
did not try another Frida version, weaken SELinux/ptrace policy, or deploy a
fallback framework. The server, helper files, and test process were removed.

Android, ADB, Magisk root, and enforcing SELinux remained healthy. Apex is
still uninstalled and unlaunched. Gate B is therefore the only supported
result: cleanup complete, Frida enumeration only, Phase16I method unresolved.

## Final result

```text
DATA_FREE_BEFORE_BYTES = 7203102720
SHARED_FREE_BEFORE_BYTES = 7182131200

USER_STORAGE_BYTES_RECOVERED = 33603584
LOCAL_TMP_BYTES_RECOVERED = 0
CACHE_BYTES_RECOVERED = 0
TOTAL_BYTES_RECOVERED = 33603584

DATA_FREE_AFTER_BYTES = 7236706304
SHARED_FREE_AFTER_BYTES = 7215734784
FUTURE_APEX_5GIB_HEADROOM = YES

POST_CLEAN_ROOT_OK = YES
POST_CLEAN_DEVICE_HEALTH_OK = YES

FRIDA_VERSION = 17.17.0
FRIDA_SERVER_ARCH = ANDROID_ARM64
FRIDA_SERVER_ARCHIVE_SHA256 = 09D1FAD867B27D69562A79289F4C412E85867F5D38AB72877036ED35E4223021

FRIDA_SERVER_DEPLOYED = YES_REMOVED_AFTER_TEST
FRIDA_SERVER_RUNNING_AS_ROOT = NO_NOT_OBSERVED

FRIDA_USB_DISCOVERY = YES
FRIDA_PROCESS_ENUMERATION = YES

TEST_PROC_MAPS_READABLE = YES
TEST_LIBC_MAPPING_FOUND = YES

FRIDA_ATTACH_TEST_PROCESS = NO_SERVER_CONNECTION_CLOSED
FRIDA_PROCESS_ARCH = NOT_OBSERVED
FRIDA_MODULE_ENUMERATION = NO_NOT_REACHED
LIBC_BASE_RESOLVED = NO_NOT_REACHED_BY_FRIDA
FRIDA_MEMORY_READ = NO_NOT_REACHED

FRIDA_NATIVE_INTERCEPTOR_ATTACH = NO_NOT_REACHED
FRIDA_NATIVE_CALL_OBSERVED = NO
FRIDA_CLEAN_DETACH = NOT_APPLICABLE_NO_SESSION

SELINUX_STILL_ENFORCING = YES
POST_TRACE_DEVICE_HEALTH_OK = YES

ROOT_NATIVE_ATTACH_CAPABILITY = PROBABLE
ROOT_CALLBACK_TRACE_PRECONDITION = UNKNOWN

SELECTED_PHASE16I_METHOD = UNRESOLVED

APEX_INSTALLED = NO
APEX_LAUNCHED = NO

CURRENT_BLOCKER = FRIDA_ATTACH_CHANNEL_CLOSES_AFTER_USB_PROCESS_ENUMERATION
NEXT_STEP = SEPARATE_AUTHORIZED_TRACE_COMPATIBILITY_DIAGNOSTIC

FINAL_GATE = B CLEAN_DEVICE_FRIDA_ENUMERATION_ONLY
COMMIT = Clean PRA-LX1 and validate root tracing Phase16H
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Privacy and safety

No device serial, runtime PID, IMEI, Android ID, account data, credential,
token, APK, OBB, firmware image, Frida binary, process map, or raw trace log is
committed. The final phone footprint contains no Phase16H Frida or ping file.
