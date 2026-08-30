# Phase16H-V - PC-only atomic ARM64 hardware-breakpoint tracer redesign

Date: 2026-08-30

## Result

Phase16H-U was reclassified from an informal address-only pre-step to its exact
ABI shape: a 24-byte atomic slot-0 submission containing the target address and
disabled control zero. Kernel source and exact C33 disassembly establish that
SETREGSET applies address before control. Disabled control skips full hardware
breakpoint validation, while active control `0x000001e5` triggers validation,
Huawei SSC normalization, and perf-event enablement.

This explains, with probable confidence, why H-U differed from H-S. H-S used a
single address-plus-active-control submission and already proved matching
address readback plus expected cached control `0x000041e4`. H-U's exact returned
address remains unknown because its runtime output did not print that value.

The redesigned tracer removes the disabled pre-step. It submits one complete
slot 0 with address and `0x000001e5`, logs the exact request and response, and
requires matching address plus `0x000041e4` before releasing the tracee gate.
Static assertions prove the 264-byte state layout and 24-byte slot-0 iovec.

Explicit disable remains a separate same-address/control-zero SET after a
future trap. Disable success no longer depends on address readback; readback is
diagnostic only. Detach is allowed only after a no-retrap SIGSTOP handshake.
All failure paths are bounded, make no more than one disable attempt, and
terminate the disposable tracee rather than detach from uncertain state.

The tracee and tracer compiled successfully as AArch64 PIE executables with
official NDK r27d, warnings treated as errors. Static layout, symbols,
disassembly, and source invariants passed. Neither executable was run. No phone,
ADB, or Apex operation occurred.

The PC-only gate is GO for requesting one separately authorized Phase16H-W
runtime attempt. This phase does not authorize that attempt.

## Evidence boundary

- Confirmed: H-U disabled pair failed readback; H-S active pair succeeded
  readback; kernel SET order is address then control; `0x000041e4` is expected
  Huawei cached normalization; redesigned layout/build invariants pass.
- Probable: the disabled validation bypass explains H-U address non-retention.
- Unknown: the exact address returned by H-U and any future SIGTRAP behavior.

## Final output

```text
H_U_SUBMISSION_CLASS = ATOMIC_ADDRESS_PLUS_DISABLED_CONTROL
H_S_SUBMISSION_CLASS = ATOMIC_ADDRESS_PLUS_ACTIVE_CONTROL

HW_BREAK_SLOT_SET_ORDER = ADDRESS_THEN_CONTROL

DISABLED_PATH_VALIDATION_SKIPPED = YES
DISABLED_PATH_ARCH_STATE_MATERIALIZED = NO
ACTIVE_CONTROL_TRIGGERS_FULL_VALIDATION = YES

ATOMIC_ACTIVE_PROGRAMMING_ALREADY_READBACK_PROVEN = YES

DISABLED_PRESTEP_REMOVED = YES

SETREGSET_IOV_LAYOUT_VALID = YES
SLOT0_ONLY_WRITE_PROVEN = YES

REQUESTED_ACTIVE_CONTROL = 0x000001e5
EXPECTED_CACHED_READBACK = 0x000041e4

EXACT_REQUEST_RETURN_ADDRESS_LOGGING_ADDED = YES

DISABLE_ADDRESS_READBACK_REQUIRED = NO

FAILURE_EXIT_CLEANUP_READY = YES

FUTURE_RUNTIME_ATTEMPT_LIMIT = ONE

ATOMIC_ACTIVE_RUNTIME_RETRY_GATE = GO

PHONE_TOUCHED = NO
ADB_USED = NO
APEX_INSTALLED = NO
APEX_LAUNCHED = NO

NEXT_STEP = SEPARATE_AUTHORIZATION_FOR_ONE_PHASE16H_W_RUNTIME_ATTEMPT

FINAL_GATE = A PC_ONLY_ATOMIC_TRACER_READY_FOR_SEPARATE_SINGLE_RUNTIME_AUTHORIZATION
COMMIT = Redesign PRA-LX1 atomic hardware breakpoint Phase16H-V
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```
