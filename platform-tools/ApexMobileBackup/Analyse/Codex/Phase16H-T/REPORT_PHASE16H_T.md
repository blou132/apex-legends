# Phase16H-T - PRA-LX1 hardware-breakpoint ABI resolution

Date: 2026-08-30

## Executive result

Phase16H-S readback `0x000041e4` is the expected Huawei normalized cached
control for requested `0x000001e5`. The exact C33 IKCONFIG enables Huawei's
address-mask extension. PRA/Lineage source and exact C33 disassembly show that
validation sets SSC to NON_SECURE (`+0x4000`) while first-enable ordering leaves
the cached enable bit clear. GETREGSET serializes this cache; it does not read a
hardware BCR.

On task schedule-in, the architecture install path forces bit 0 and is expected
to write BCR `0x000041e5`. That value is source-derived, not runtime-observed.
Supplying a zero control disables the perf event. If installed, PMU delete
reaches ARM64 uninstall and writes zero to the BCR; if inactive, disabled state
prevents later installation. Cached GETREGSET fields can remain `0x41e4`, so a
full-zero readback is not a valid cleanup requirement.

Thread exit/exec flushes ptrace hardware breakpoints. `PTRACE_DETACH` alone does
not: exact C33 `ptrace_disable` only disables single-step. A future test must
disable before detach and prove no retrap through a controlled tracee handshake.

No phone, ADB, Apex, or runtime target was touched. The evidence supports one
future bounded disposable-target test, with no software POKETEXT and no Apex.

## Evidence classification

- `CONFIRMED`: requested and returned control decodes.
- `CONFIRMED`: SSC normalization and cached enabled-bit ordering.
- `CONFIRMED`: GETREGSET uses cached perf state.
- `CONFIRMED`: exact C33 install forces enable and uninstall writes BCR zero.
- `CONFIRMED`: exact C33 exit flushes; detach alone does not flush.
- `SOURCE_DERIVED_ONLY`: actual installed BCR is expected to be `0x41e5`.
- `NOT_YET_PROVEN`: SIGTRAP at function entry and x0-x7 capture.
- `MEDIUM`: exact C33 source-tree match; no byte-identical source/build exists.

## Final output

```text
REQUESTED_CTRL_DECODE = ENABLED_EL0_EXECUTE_LEN4_SSC0_MASK0
READBACK_CTRL_DECODE = DISABLED_CACHED_EL0_EXECUTE_LEN4_SSC_NON_SECURE_MASK0

SSC_NORMALIZATION_DELTA = 0x00004000
READBACK_PLUS_0X4000_EXPLAINED = YES

CACHED_ENABLE_BIT_ZERO_EXPLAINED = YES

GETREGSET_CTRL_SOURCE = CACHED_PERF_STATE

HARDWARE_ENABLE_FORCED_ON_INSTALL = YES
EXPECTED_ACTUAL_BCR = 0x000041e5_SOURCE_DERIVED_ONLY

ADDRESS_PATH_EXPLAINED = YES

USER_CTRL_ZERO_DISABLE_PATH = PERF_DISABLE_THEN_DISABLED_ATTRIBUTE
HARDWARE_BCR_CLEAR_EXPECTED = YES_IF_INSTALLED_OTHERWISE_ALREADY_ABSENT
CLEAR_SEQUENCE_CONFIDENCE = HIGH

GETREGSET_CTRL_ZERO_NOT_REQUIRED_FOR_SAFE_DISABLE = YES

EXIT_CLEANUP_PATH_CONFIDENCE = HIGH
DETACH_CLEANUP_PATH_CONFIDENCE = HIGH_NEGATIVE_DETACH_ALONE_DOES_NOT_FLUSH

PRA_8X_SOURCE_CORRELATION = SEMANTICALLY_EQUIVALENT_FOR_TARGETED_ABI_PATHS
EXACT_C33_SOURCE_MATCH_CONFIDENCE = MEDIUM

PHASE16H_S_READBACK_REINTERPRETATION = SETREGSET_SUCCESS_READBACK_EXPECTED_NORMALIZATION

HW_BREAKPOINT_RETRY_GATE = GO

PHONE_TOUCHED = NO
APEX_INSTALLED = NO
APEX_LAUNCHED = NO

NEXT_STEP = ONE_BOUNDED_DISPOSABLE_HW_BREAKPOINT_EXECUTION_TEST_NO_APEX
FINAL_GATE = A PC_ONLY_ABI_RESOLVED_SINGLE_RUNTIME_RETRY_JUSTIFIED
COMMIT = Resolve PRA-LX1 hardware breakpoint semantics Phase16H-T
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```
