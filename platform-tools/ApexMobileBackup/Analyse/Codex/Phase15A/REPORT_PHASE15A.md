# Phase15A - Windows WHPX read-only host audit

Date: 2026-08-20

## Executive result

The Windows 11 25H2 x86_64 host has an AMD Ryzen 7 3700U with firmware
virtualization, SLAT, and VM monitor extensions enabled. Windows Hypervisor
Platform is available but disabled. Virtual Machine Platform is also disabled,
the full Hyper-V role is not listed on this Home edition, VBS/HVCI are inactive,
and Windows reports no currently present hypervisor.

Android Emulator 36.4.9.0 returns acceleration code `6`, reporting no installed
Android Emulator hypervisor driver. AEHD and HAXM are absent. VirtualBox 7.2.4
and its drivers are present, but no evidence establishes it as the current
blocker. `ApexPhase9Lab` remains configured for the Android 36.1 Google Play
x86_64 image with ARM64 translation through `libndk_translation.so`.

The minimum demonstrated change is to enable Windows Hypervisor Platform and
restart. Phase15A performed no feature change, BCD change, installation,
restart, AVD launch, Apex launch, or phone command.

## Required results

```text
WINDOWS_EDITION = Windows 11 Home Single Language
WINDOWS_VERSION = 25H2
WINDOWS_BUILD = 26200.9168
HOST_ARCH = x86_64
CPU_VENDOR = AMD
CPU_MODEL = AMD Ryzen 7 3700U with Radeon Vega Mobile Gfx
VIRTUALIZATION_FIRMWARE_ENABLED = CONFIRMED YES
SLAT_SUPPORTED = CONFIRMED YES
VM_MONITOR_MODE_SUPPORTED = CONFIRMED YES
HYPERVISOR_LAUNCH_TYPE = UNKNOWN_ACCESS_DENIED
WHPX_AVAILABLE = CONFIRMED YES
WHPX_FEATURE_STATE = CONFIRMED DISABLED
HYPER_V_FEATURE_STATE = NOT_LISTED_ON_THIS_EDITION
VIRTUAL_MACHINE_PLATFORM_STATE = CONFIRMED DISABLED
VBS_STATE = CONFIRMED DISABLED
MEMORY_INTEGRITY_STATE = CONFIRMED DISABLED
AEHD_INSTALLED = CONFIRMED NO
AEHD_STATE = NOT_INSTALLED
EMULATOR_VERSION = 36.4.9.0
ACCEL_CHECK_RESULT = FAILED_CODE_6_NO_HYPERVISOR_DRIVER
APEX_PHASE9_LAB_PRESENT = CONFIRMED YES
REQUIRED_CHANGE = ENABLE_WINDOWS_HYPERVISOR_PLATFORM
REBOOT_REQUIRED = YES
FINAL_GATE = CONFIRMED B WHPX_ENABLE_REQUIRED
```

## Evidence limits

The non-elevated BCD read was denied, so `hypervisorlaunchtype` remains unknown.
No boot-setting change is justified unless WHPX still fails after feature
enablement and reboot. All published data is cleaned; hostname, account data,
local absolute paths, and full host command output are excluded.
