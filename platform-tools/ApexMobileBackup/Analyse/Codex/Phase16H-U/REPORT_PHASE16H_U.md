# Phase16H-U - single ARM64 hardware-breakpoint runtime validation

Date: 2026-08-30

## Result

The single authorized PRA-LX1 attempt stopped safely before active breakpoint
programming. Root attach, the 272-byte general register set, the 264-byte ARM64
hardware-breakpoint register set, and six clear execution slots were confirmed.
The tracee binary and exact runtime target correlation were valid.

The tracer then submitted slot 0 as target address plus disabled control zero.
`PTRACE_SETREGSET` returned success, but immediate GETREGSET did not retain the
target address. The tracer did not submit `0x1e5`, did not create the start
gate, did not continue the process, and did not receive SIGTRAP. It terminated
the stopped disposable tracee through the required safety path. No retry was
performed.

The tested disabled address-only pre-step differs from Phase16H-S, where a
single address plus active-control submission returned the address correctly.
This is the current blocker, but the exact cause remains unresolved because the
unexpected returned address was not printed. A PC-only tracer redesign is the
only next step; Phase16I remains blocked.

All temporary device artifacts were removed. Root, enforcing SELinux, Android,
ADB, USB, and the launcher remained healthy. Apex stayed absent and unlaunched.

## Final output

```text
PHASE16H_U_PREFLIGHT = PASS

TRACE_TARGET_RUNTIME_VALIDATED = YES

PTRACE_ATTACH_RESULT = SUCCESS
REGISTER_SET_READY = YES

HW_BREAK_INITIAL_READ_OK = YES
HW_EXEC_BREAKPOINT_SLOTS = 6

HW_BREAK_ADDR_SET_RESULT = SUCCESS
HW_BREAK_ADDR_READBACK_MATCH = NO

HW_BREAK_CTRL_SET_RESULT = NOT_REACHED
HW_BREAK_CTRL_READBACK = NOT_REACHED
HW_BREAK_NORMALIZED_READBACK_EXPECTED = NOT_REACHED

HW_BREAKPOINT_ARMED_FOR_TEST = NO

SIGTRAP_RECEIVED = NO_NOT_CONTINUED
SIGTRAP_CODE = NOT_CAPTURED
SIGTRAP_ADDRESS_MATCH = NOT_REACHED

EXACT_FUNCTION_ENTRY_TRAP = NO_NOT_REACHED

PC_CAPTURED = NO_NOT_REACHED
SP_CAPTURED = NO_NOT_REACHED
LR_CAPTURED = NO_NOT_REACHED

X0_MATCH = NOT_REACHED
X1_MATCH = NOT_REACHED
X2_MATCH = NOT_REACHED
X3_MATCH = NOT_REACHED
X4_MATCH = NOT_REACHED
X5_MATCH = NOT_REACHED
X6_MATCH = NOT_REACHED
X7_MATCH = NOT_REACHED

FUNCTION_ARGUMENT_CAPTURE = NO_NOT_REACHED

HW_BREAK_DISABLE_SET_RESULT = NOT_REQUIRED_ACTIVE_CONTROL_NEVER_SET
HW_BREAK_DISABLED_BY_PROVEN_PATH = NOT_APPLICABLE_THREAD_EXIT_CLEANUP

POST_DISABLE_FUNCTION_EXECUTION_OBSERVED = NO_NOT_REACHED
POST_DISABLE_RETRAP = NOT_APPLICABLE_NOT_CONTINUED

PTRACE_DETACH_RESULT = NOT_PERFORMED_SAFETY_TERMINATED
TRACEE_CONTINUED_AFTER_DETACH = NO_NOT_DETACHED
TRACEE_FUNCTION_RESULT_STILL_CORRECT = NO_NOT_EXECUTED
TRACEE_TERMINATED_CLEANLY = NO_PTRACE_SAFETY_KILL_USED

ROOT_PTRACE_FUNCTION_ENTRY_CAPABILITY = NO_NOT_PROVEN
ROOT_CALLBACK_TRACE_PRECONDITION = NO

PHASE16I_READY = NO

ROOT_STILL_HEALTHY = YES
SELINUX_STILL_ENFORCING = YES
POST_TEST_DEVICE_HEALTH_OK = YES

APEX_INSTALLED = NO
APEX_LAUNCHED = NO

CURRENT_BLOCKER = DISABLED_ADDRESS_ONLY_SET_NOT_RETAINED
NEXT_STEP = PC_ONLY_ATOMIC_PROGRAMMING_REDESIGN_NO_RUNTIME
FINAL_GATE = E PTRACE_OR_REGSET_RUNTIME_FAILURE
COMMIT = Validate PRA-LX1 hardware breakpoint runtime Phase16H-U
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```
