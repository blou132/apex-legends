# Phase16B - PRA-LX1 callback trace capability audit

Date: 2026-08-27

## Executive result

The authorized PRA-LX1 is a production Android 8 device with
`ro.debuggable=0`, `ro.secure=1`, an unprivileged ADB shell, no root facility,
SELinux enforcing, `/proc` `hidepid=2`, and restrictive perf policy. The
installed Apex package is neither debuggable nor profileable by shell and is
ineligible for `run-as`/JDWP debugging.

`debuggerd -b` is advertised, but Phase14 already proved that the OS denies a
process dump for this package. `showmap`, simpleperf, perf, Perfetto, traced,
and heapprofd are absent. `atrace`, `am profile`, and package ART profile
commands are present but cannot expose arbitrary native PCs, AArch64 registers,
callback/vtable pointers, or an indirect `BLR` destination.

No supported non-invasive method can resolve the external callback slot
`+0x28`. The strict stop rule applies; no Apex launch or escalation occurred.

## Required result

```text
APEX_DEBUGGABLE = NO
APEX_PROFILEABLE = NO
APEX_PROFILEABLE_BY_SHELL = NO
APEX_JDWP_AVAILABLE_WHEN_RUNNING = NO_BY_NONDEBUGGABLE_POLICY

FUTURE_LIBGCLOUD_MAPPING_OBSERVABLE = NO
FUTURE_LIBUE4_MAPPING_OBSERVABLE = NO

NATIVE_CALL_STACK_AVAILABLE = NO
REGISTER_ARGUMENT_TRACE_AVAILABLE = NO
INDIRECT_CALL_TARGET_TRACE_AVAILABLE = NO

ROOT_REQUIRED_FOR_USEFUL_TRACE = YES

NONINVASIVE_RUNTIME_TRACE_AVAILABLE = NO
BEST_FUTURE_TRACE_METHOD = NO_NONINVASIVE_METHOD

FUTURE_PHASE16C_TARGET = NONE_UNDER_CURRENT_CONSTRAINTS
FUTURE_PHASE16C_EXPECTED_EVIDENCE = NONE

APEX_LAUNCHED = NO
APEX_MODIFIED = NO

CURRENT_BLOCKER = PRODUCTION_DEVICE_POLICY_PREVENTS_EXACT_EXTERNAL_CALLBACK_OBSERVATION

FINAL_GATE = D NO_NONINVASIVE_RUNTIME_TRACE_AVAILABLE
COMMIT = Audit PRA-LX1 callback trace capability Phase16B
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

Only cleaned capability metadata is committed. No ADB serial, IMEI, Android
ID, MAC, account identifier, token, PID, raw package dump, or private runtime
address is published.
