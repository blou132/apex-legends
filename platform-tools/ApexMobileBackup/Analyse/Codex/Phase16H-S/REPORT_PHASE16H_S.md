# Phase16H-S - ARM64 function-entry breakpoint validation

Date: 2026-08-30

## Executive result

The clean-room ARM64 tracee, symbol, PT_LOAD correlation, load bias, executable
mapping, and first instruction were all validated. Root ptrace again attached
and returned the expected 272-byte general register set.

The software breakpoint path stopped safely: the one authorized
`PTRACE_POKETEXT` call returned EIO before changing memory. The probe detached,
and the tracee continued to produce the correct deterministic function result.

The hardware fallback confirmed a 264-byte `NT_ARM_HW_BREAK` regset with six
execution slots. `PTRACE_SETREGSET` returned success, but the Huawei kernel did
not return the requested control state. On the corrected bounded retry, the
address matched while control `0x000001e5` returned as `0x000041e4`. Because
exact arming and removal could not be proven, the probe did not continue either
tracee to the target. It terminated only those stopped disposable processes.

No SIGTRAP occurred and no function-entry arguments were captured. Phase16I is
therefore not ready. Gate C is the supported outcome: register attach works,
but both breakpoint methods remain unavailable or unresolved.

## Final result

```text
PHASE16H_S_PREFLIGHT = PASS

TRACEE_ARCH = AARCH64
TRACE_TARGET_SYMBOL_FOUND = YES
LOAD_BIAS_RESOLVED = YES
TRACE_TARGET_RUNTIME_VALIDATED = YES

PTRACE_ATTACH_RESULT = SUCCESS
ORIGINAL_WORD_SAVED = YES

SOFTWARE_BREAKPOINT_SUPPORTED = NO
BREAKPOINT_WRITE_RESULT = FAILURE_EIO
BREAKPOINT_READBACK_VALID = NOT_APPLICABLE_NO_WRITE

SIGTRAP_RECEIVED = NO_NOT_CONTINUED
EXACT_FUNCTION_ENTRY_TRAP = NO_NOT_REACHED

PC_CAPTURED = NO_NOT_AT_FUNCTION_ENTRY
SP_CAPTURED = NO_NOT_AT_FUNCTION_ENTRY
LR_CAPTURED = NO_NOT_AT_FUNCTION_ENTRY

X0_MATCH = NO_NOT_REACHED
X1_MATCH = NO_NOT_REACHED
X2_MATCH = NO_NOT_REACHED
X3_MATCH = NO_NOT_REACHED
X4_MATCH = NO_NOT_REACHED
X5_MATCH = NO_NOT_REACHED
X6_MATCH = NO_NOT_REACHED
X7_MATCH = NO_NOT_REACHED

FUNCTION_ARGUMENT_CAPTURE = NO

ORIGINAL_INSTRUCTION_RESTORED = NOT_APPLICABLE_WRITE_FAILED
RESTORE_READBACK_MATCH = NOT_APPLICABLE_WRITE_FAILED
PC_CORRECTION_REQUIRED = NOT_DETERMINED_NO_TRAP
PC_CORRECTION_APPLIED = NO

SINGLESTEP_RESULT = NO_NOT_REACHED
ORIGINAL_INSTRUCTION_EXECUTED = NO_NOT_REACHED

PTRACE_DETACH_RESULT = SUCCESS_SOFTWARE_PATH
TRACEE_CONTINUED_AFTER_DETACH = YES_SOFTWARE_PATH
TRACEE_FUNCTION_RESULT_STILL_CORRECT = YES_SOFTWARE_PATH

SOFTWARE_FUNCTION_ENTRY_BREAKPOINT = FAILURE_WRITE_UNSUPPORTED

ARM64_HW_BREAK_REGSET_SUPPORTED = YES
HW_EXEC_BREAKPOINT_SLOTS = 6
HW_BREAKPOINT_PROGRAMMED = SETREGSET_SUCCESS_READBACK_INVALID
HW_BREAKPOINT_SIGTRAP = NO_NOT_CONTINUED
HW_FUNCTION_ARGUMENT_CAPTURE = NO_NOT_REACHED
HW_BREAKPOINT_REMOVED = NO_RESTORE_NOT_VALIDATED
HW_TRACEE_CONTINUES = NO_SAFETY_TERMINATED

ROOT_PTRACE_FUNCTION_ENTRY_CAPABILITY = NO_NOT_PROVEN

SELECTED_PHASE16I_BREAKPOINT_METHOD = UNRESOLVED

ROOT_CALLBACK_TRACE_PRECONDITION = NOT_SATISFIED

PHASE16I_READY = NO

SELINUX_STILL_ENFORCING = YES
ROOT_STILL_HEALTHY = YES
POST_TEST_DEVICE_HEALTH_OK = YES

APEX_INSTALLED = NO
APEX_LAUNCHED = NO

CURRENT_BLOCKER = SOFTWARE_POKETEXT_EIO_AND_VENDOR_HW_BREAK_CONTROL_ABI_UNRESOLVED
NEXT_STEP = RESOLVE_EXACT_HUAWEI_4_4_HW_BREAK_REGSET_SEMANTICS_PC_ONLY
FINAL_GATE = C REGISTER_ATTACH_ONLY_BREAKPOINT_UNAVAILABLE
COMMIT = Validate ARM64 ptrace function entry Phase16H-S
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Safety interpretation

The hardware `SETREGSET` syscall returning success is not enough to claim a
programmed breakpoint. Exact control readback differed, no target execution was
allowed, and no trap occurred. The tracees were killed because task exit is the
safe boundary for unresolved per-thread debug state.

No absolute runtime address, raw map, PID, device identifier, binary, APK, OBB,
full log, or dump is committed. Apex remained uninstalled and unlaunched.
