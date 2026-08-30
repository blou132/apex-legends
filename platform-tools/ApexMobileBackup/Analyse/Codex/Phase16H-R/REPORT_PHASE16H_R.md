# Phase16H-R - PRA-LX1 native tracing diagnosis

Date: 2026-08-30

## Executive result

Official Frida 17.17.0 was revalidated with exact client/server version and
hash matching. Under supervision, the root server reproducibly crashed with a
null-pointer SIGSEGV during ART/helper initialization, before enumeration or a
minimal attach could run. New Frida tombstones were created. No current-run AVC
denial or stable helper process was observed, so SELinux is not a supported
cause for this specific crash.

Upstream issue 3707 and its fix were checked against the exact 17.17.0 source
revision. The fix is included, so the PRA failure cannot simply be labeled as
that upstream regression. The evidence nevertheless justified one official
pre-17.6 comparator. Frida 17.5.2 stayed alive but both enumeration and exact
PID attach failed at its write-based `ptrace pokedata` operation with EIO.

The independent path succeeded. Built-in debuggerd obtained a bounded ARM64
stack from disposable ping. Two tiny ARM64 programs built with official Google
NDK r27d then proved root `PTRACE_ATTACH`, stopped-state wait,
`PTRACE_GETREGSET` (272 bytes), and clean `PTRACE_DETACH`. The tracee continued
normally afterward and SELinux remained enforcing.

Gate C is therefore supported: generic root native attach/read-register/detach
works, while Frida is unusable on this device. The selected future method is a
root ptrace native-debug path. It is not yet Phase16I-ready because bounded
function-entry/register observation has not been proven on a disposable target.

## Final result

```text
FRIDA_17_17_VERSION_MATCH = YES
FRIDA_SERVER_UID = root
FRIDA_SERVER_SELINUX_CONTEXT = u:r:magisk:s0

FRIDA_ENUMERATION_REPEAT_SUCCESS = NO_SERVER_CRASHED_BEFORE_ENUMERATION
FRIDA_SESSION_CREATION = NOT_ATTEMPTED_SERVER_STARTUP_CRASH

FRIDA_SERVER_STILL_ALIVE_AFTER_ATTACH = NOT_APPLICABLE_CRASHED_BEFORE_ATTACH
FRIDA_SERVER_EXIT_CODE = 139
FRIDA_SERVER_FAILURE_CLASS = SIGSEGV_NULL_DEREFERENCE_DURING_ART_HELPER_INITIALIZATION

FRIDA_HELPER_OBSERVED = DEX_CREATION_ONLY_NO_STABLE_PROCESS
FRIDA_HELPER_CONTEXT = NOT_OBSERVED_CURRENT_RUN
FRIDA_HELPER_FAILURE_CORRELATED = YES_PROBABLE_ART_HELPER_INITIALIZATION

LOGCAT_FRIDA_FATAL = YES_SIGSEGV
LOGCAT_HELPER_FATAL = NO_SEPARATE_HELPER_PROCESS_FATAL
SELINUX_CAUSAL_EVIDENCE = NO_CURRENT_RUN_AVC

NEW_FRIDA_TOMBSTONE = YES
FRIDA_CRASH_SIGNAL = SIGSEGV

UPSTREAM_HELPER_FIX_INCLUDED_IN_17_17 = YES

DEBUGGERD_TEST_PROCESS_STACK_OBTAINED = YES

ANDROID_NDK_AVAILABLE = YES_OFFICIAL_R27D_27_3_13750724

PTRACE_ATTACH_RESULT = SUCCESS
PTRACE_GETREGSET_RESULT = SUCCESS_272_BYTES
PTRACE_DETACH_RESULT = SUCCESS

FRIDA_17_5_2_TEST_AUTHORIZED_BY_EVIDENCE = YES
FRIDA_17_5_2_ATTACH = FAILURE_PTRACE_POKEDATA_EIO

FRIDA_MODULE_ENUMERATION = NO_NOT_REACHED
FRIDA_MEMORY_READ = NO_NOT_REACHED
FRIDA_NATIVE_INTERCEPTOR = NO_NOT_REACHED
FRIDA_NATIVE_CALL_OBSERVED = NO

ROOT_NATIVE_ATTACH_CAPABILITY = YES
ROOT_CALLBACK_TRACE_PRECONDITION = PROBABLE_NOT_VALIDATED

SELECTED_PHASE16I_METHOD = ROOT_PTRACE_NATIVE_DEBUG_PATH

SELINUX_STILL_ENFORCING = YES
SECURITY_BASELINE_PRESERVED = YES
POST_TEST_DEVICE_HEALTH_OK = YES

APEX_INSTALLED = NO
APEX_LAUNCHED = NO

CURRENT_BLOCKER = ROOT_PTRACE_FUNCTION_ENTRY_OBSERVATION_NOT_YET_VALIDATED
NEXT_STEP = VALIDATE_BOUNDED_FUNCTION_ENTRY_REGISTER_OBSERVATION_ON_DISPOSABLE_ARM64_TARGET
FINAL_GATE = C INDEPENDENT_PTRACE_READY_FRIDA_UNUSABLE
COMMIT = Diagnose PRA-LX1 native tracing Phase16H-R
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Interpretation boundary

The current 17.17.0 failure occurs before attach. The historical Phase16H
helper AVC remains real but does not establish the cause of the current crash.
Likewise, Frida 17.5.2's `ptrace pokedata` EIO does not mean that every ptrace
operation is blocked: the independent attach/getregs/detach probe succeeded.

No native callback was observed. No breakpoint, instruction write, argument
capture, module enumeration, Frida memory read, hook, or Apex attach occurred.

## Privacy and cleanup

No serial number, runtime PID, IMEI, Android ID, account data, token, APK, OBB,
firmware, NDK archive, Frida binary, test binary, raw log, process map, or full
tombstone is committed. Current-run phone artifacts were removed. Apex remains
absent and the device remains healthy with Magisk root and enforcing SELinux.
