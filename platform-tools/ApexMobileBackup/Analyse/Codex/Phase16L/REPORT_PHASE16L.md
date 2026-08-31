# Phase16L - libUE4 Dolphin PLT dispatch

## Executive result

Phase16L reconstructs the exact machine edge without relying on Ghidra's PLT
labels. `CreateDolphin` is dynamic symbol 375 and `.rela.plt` entry 529. Its
`R_AARCH64_JUMP_SLOT` targets GOT `0x0b37e8c8`. Four unrelocated dummy stubs
earlier in `.plt` adjust the physical index, placing the real stub at ELF
`0x0a3c18f0` / Ghidra `0x0a4c18f0`. Raw stub instructions load that exact GOT
cell, and three neighboring controls map consistently. There is no PLT symbol
shift.

The raw `BL` word at ELF `0x05a314cc` is `0x95264109`; manual AArch64 decoding
also targets `0x0a3c18f0`. An exact-target scan of `.text` finds this one call
and no thunk or veneer. Unwind metadata confirms its function range as
`0x05a311a8...0x05a31ca8`, matching Ghidra after the validated bias.

The call is genuinely `CreateDolphin`, but it is not a persistent owner. The
factory result moves from `x0` to `x1`, then the next exact branch invokes the
noreturn imported helper `std::__throw_length_error(char const*)`; no store,
return passthrough, release, vptr load, or Init dispatch occurs. No callsite
relocation changes this sequence. Static recovery therefore reaches level C:
the real callsite is resolved, but object ownership is lost.

## Final result

```text
PHASE16L_PREFLIGHT = PASS

LIBUE4_EXACT_INPUT_VALID = YES
LIBGCLOUD_EXACT_INPUT_VALID = YES

LIBUE4_ADDRESS_MODEL_VALIDATED = YES

CREATEDOLPHIN_DYNSYM_INDEX = 375
CREATEDOLPHIN_RELA_PLT_INDEX = 529
CREATEDOLPHIN_GOT_CELL = 0x0b37e8c8

CREATEDOLPHIN_EXPECTED_PLT_ELF = 0x0a3c18f0
CREATEDOLPHIN_EXPECTED_PLT_GHIDRA = 0x0a4c18f0

CREATEDOLPHIN_PLT_STUB_DECODE_VALID = YES
PLT_TO_GOT_MATCH = YES
PLT_INDEX_MAPPING_CONSISTENT = YES
PLT_ATTRIBUTION_SHIFT = NONE

COMPUTED_CALL_SITE_CLASS = FINAL_BR_IN_STANDARD_AARCH64_PLT_STUB
COMPUTED_CALL_REAL_TARGET_SYMBOL = CreateDolphin

DIRECT_CALL_RAW_OPCODE = 0x95264109
DIRECT_CALL_MANUAL_TARGET_ELF = 0x0a3c18f0
DIRECT_CALL_REAL_IMPORTED_SYMBOL = CreateDolphin

PHASE16K_DIRECT_CALL_ATTRIBUTION = CONFIRMED

GHIDRA_FUNCTION_BOUNDARY_CORRECT = YES
CALLSITE_THUNK_PRESENT = NO
CALLSITE_VENEER_PRESENT = NO

REAL_CREATEDOLPHIN_CALLSITE_COUNT = 1
REAL_CREATEDOLPHIN_CALLSITE_CLASSIFICATION = ERROR_HELPER_PATH

CREATEDOLPHIN_RETURN_FLOW = x0 -> x1 -> unused by noreturn throw helper; pointer lost
DOLPHIN_OBJECT_STORAGE = NONE_AT_PROVEN_CALLSITE
DOLPHIN_OWNER_FIELD = NONE

DOLPHIN_INIT_DISPATCH_PROVEN = NO
DOLPHIN_INIT_CALLER = UNKNOWN
DOLPHIN_INIT_CALL_SITE = UNKNOWN
DOLPHIN_INIT_TARGET_MATCH = NOT_APPLICABLE

DOLPHIN_INIT_SELECTION_GATE = UNKNOWN
DOLPHIN_ALTERNATIVE_BRANCH = UNKNOWN

PHASE16K_PLT_ANOMALY_ROOT_CAUSE = OTHER_PROVEN: NO_PLT_MISATTRIBUTION; REAL_CALL_RETURN_ENTERS_NORETURN_ERROR_PATH; PRODUCER_REASON_UNKNOWN
STATIC_RECOVERY_LEVEL = C REAL_CREATEDOLPHIN_CALLSITE_RESOLVED_OBJECT_FLOW_LOST

THREAD10_CVERSIONMGR_EXECUTOR = CONFIRMED
CVERSIONMGR_EXECUTING_THREAD_CONFIDENCE = HIGH

PHONE_TOUCHED = NO
ADB_USED = NO
APEX_LAUNCHED = NO
APPDATA_TOUCHED = NO
ACTIVE_HW_BREAKPOINT_COUNT = 0
MEMORY_WRITE_USED = NO
APK_MODIFIED = NO
OBB_MODIFIED = NO

FUTURE_ACTIVE_TRACE_GATE = NO_GO
FUTURE_TRACE_TRIGGER_STRATEGY = NONE_DETERMINISTIC

CURRENT_BLOCKER = ONLY_REAL_CREATEDOLPHIN_CALLSITE_DISCARDS_RETURN_BEFORE_PERSISTENT_OWNERSHIP
NEXT_STEP = REQUEST_SEPARATE_EVIDENCE_FOR_RUNTIME_CREATEDOLPHIN_OWNER_OR_COMPARABLE_PRESERVED_STATE
FINAL_GATE = C REAL_CREATEDOLPHIN_CALLSITE_RESOLVED_OBJECT_FLOW_LOST
COMMIT = Resolve libUE4 Dolphin PLT dispatch Phase16L
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Safety boundary

No phone, ADB, Apex launch, AppData access, runtime instrumentation, active
breakpoint, memory write, APK/OBB/SO modification, network access, or Samsung
access occurred. No raw proprietary disassembly dump or local machine path is
committed.
