# Phase16H-W - single atomic ARM64 hardware-breakpoint runtime validation

Date: 2026-08-30

## Result

The single authorized attempt on the rooted PRA-LX1 succeeded end to end. The
exact committed Phase16H-V sources built as AArch64 PIE with NDK r27d, passed
static review, deployed with matching hashes, and operated only on the
disposable tracee.

The tracer attached to the correct stopped tracee, validated the 272-byte GPR
state, 264-byte hardware-breakpoint state, six execution slots, and clear
initial controls. It then consumed the one attempt with a single 24-byte slot-0
SETREGSET containing the fresh `trace_target` runtime address and active control
`0x000001e5`.

SETREGSET succeeded. Immediate readback matched the address and returned
expected Huawei cached control `0x000041e4`. Only then was the start gate
released. The tracee stopped with SIGTRAP code 4 (`TRAP_HWBKPT`) at the exact
function-entry address. PC matched the target, SP/LR were captured, and X0-X7
all matched the eight committed deterministic arguments.

The separate explicit disable SET succeeded. Its diagnostic GETREGSET retained
cached fields, including control `0x000041e5`, but these were correctly not used
as disable criteria. After continuation, the trapped call and a second target
call both returned the expected result, and the tracee reached controlled
SIGSTOP without retrapping. Detach then succeeded. A post-detach target call
also returned correctly and the tracee remained healthy during bounded
observation.

The tracee was terminated normally, all temporary device artifacts were
removed, and no test process remained. ADB, USB, Android, uid-0 root, enforcing
SELinux, battery health, and system_server remained healthy. Apex stayed absent
and unlaunched. No software breakpoint, memory write, patch, injection, policy
change, network operation, Samsung access, or second runtime attempt occurred.

The disposable ARM64 function-entry/register capture capability and callback
trace precondition are now proven. Phase16I is ready for separate authorization;
it was not started by this phase.

## Final output

```text
PHASE16H_W_PREFLIGHT = PASS

TRACE_SOURCE_UNCHANGED = YES
BUILD_SUCCESS = YES

TRACEE_HASH_MATCH = YES
TRACER_HASH_MATCH = YES

TRACE_TARGET_RUNTIME_VALIDATED = YES

PTRACE_ATTACH_RESULT = SUCCESS
GENERAL_REGSET_VALID = YES
HW_REGSET_VALID = YES
HW_EXEC_SLOTS = 6
HW_INITIAL_STATE_CLEAR = YES

ACTIVE_PROGRAMMING_READY = YES

RUNTIME_ATTEMPT_COUNT = 1
RUNTIME_ATTEMPT_LIMIT = ONE

ACTIVE_SETREGSET_RESULT = SUCCESS

ACTIVE_ADDRESS_READBACK_MATCH = YES
ACTIVE_CTRL_READBACK = 0x000041e4
ACTIVE_CTRL_NORMALIZATION_MATCH = YES

HW_BREAKPOINT_ARMED_FOR_TEST = YES

SIGTRAP_RECEIVED = YES
SIGTRAP_CODE = 4
SIGTRAP_HW_CLASSIFICATION = TRAP_HWBKPT
SIGTRAP_ADDRESS_MATCH = YES

PC_CAPTURED = YES
SP_CAPTURED = YES
LR_CAPTURED = YES
EXACT_FUNCTION_ENTRY_TRAP = YES

X0_MATCH = YES
X1_MATCH = YES
X2_MATCH = YES
X3_MATCH = YES
X4_MATCH = YES
X5_MATCH = YES
X6_MATCH = YES
X7_MATCH = YES

FUNCTION_ARGUMENT_CAPTURE = YES

HW_BREAK_DISABLE_SET_RESULT = SUCCESS
DISABLE_READBACK_DIAGNOSTIC_ONLY = YES

POST_DISABLE_FUNCTION_EXECUTION_OBSERVED = YES
POST_DISABLE_RESULT_CORRECT = YES
POST_DISABLE_RETRAP = NO
POST_DISABLE_HANDSHAKE_REACHED = YES

PTRACE_DETACH_RESULT = SUCCESS

TRACEE_CONTINUED_AFTER_DETACH = YES
POST_DETACH_FUNCTION_RESULT_CORRECT = YES

ROOT_PTRACE_FUNCTION_ENTRY_CAPABILITY = YES
ROOT_CALLBACK_TRACE_PRECONDITION = YES
PHASE16I_READY = YES

ROOT_STILL_HEALTHY = YES
SELINUX_STILL_ENFORCING = YES
POST_TEST_DEVICE_HEALTH_OK = YES

APEX_INSTALLED = NO
APEX_LAUNCHED = NO

CURRENT_BLOCKER = NONE_FOR_DISPOSABLE_FUNCTION_ENTRY_TRACE
NEXT_STEP = SEPARATE_AUTHORIZATION_FOR_BOUNDED_PHASE16I_APEX_CALLBACK_TRACE
FINAL_GATE = A HW_BREAKPOINT_FUNCTION_ENTRY_ARGUMENT_CAPTURE_READY
COMMIT = Validate atomic PRA-LX1 hardware breakpoint Phase16H-W
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```
