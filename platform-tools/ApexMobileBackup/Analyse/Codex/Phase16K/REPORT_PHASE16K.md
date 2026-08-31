# Phase16K - client Dolphin owner and bootstrap selection

## Executive result

The exact `CreateDolphin` factory is a 40-byte global export at ELF
`0x0044514c` / Ghidra `0x0054514c`. It allocates and constructs the known
`GCloudDolphinImp` object with primary vptr `0x00979620`. Targeted ELF metadata
proves that `libUE4.so` is the only preserved client library importing this
factory, through an `R_AARCH64_JUMP_SLOT` relocation.

The sole decoded direct client call cannot be promoted as the bootstrap owner.
Its surrounding function systematically uses the PLT entry preceding the API
matching each argument shape; the factory return is passed to an incompatible
adjacent helper instead of being retained or virtually dispatched. The upper
caller is identified as the post-extraction callback
`DolphinCallback::OnDolphinFirstExtractSuccess`, not an Init owner. This
candidate is therefore explicitly invalidated.

No valid returned-object store, client virtual dispatch to slot `+0x10`, Init
caller, or controlling selector was proven. The exact cross-module boundary
is resolved, but the semantic dispatch representation is still opaque. Static
dependence on AppData or a remote value remains unknown, trigger
reproducibility is not determined, and future active tracing remains `NO_GO`.

## Final result

```text
PHASE16K_PREFLIGHT = PASS

LIBGCLOUD_EXACT_INPUT_VALID = YES
GHIDRA_READ_ONLY = YES

CREATEDOLPHIN_GHIDRA = 0x0054514c
CREATEDOLPHIN_ELF_VA = 0x0044514c
CREATEDOLPHIN_SYMBOL_CLASS = FUNC GLOBAL DEFAULT; dynamic export; size 40
CREATEDOLPHIN_CONSTRUCTS_GCLOUDDOLPHINIMP = YES

CREATEDOLPHIN_REFERENCE_COUNT = 3
CREATEDOLPHIN_CODE_CALLERS = 1 exact; 0 proven semantic owners
CREATEDOLPHIN_ADDRESS_TAKEN = NO

CREATEDOLPHIN_DYNAMIC_LOOKUP = NO
CREATEDOLPHIN_REGISTRATION_KIND = NONE_FOUND
CREATEDOLPHIN_REGISTRATION_OWNER = NONE
CREATEDOLPHIN_FACTORY_CONSUMER = UNKNOWN

DOLPHIN_OBJECT_STORAGE = NOT_PROVEN
DOLPHIN_OWNER_OBJECT = UNKNOWN
DOLPHIN_OWNER_FIELD = UNKNOWN

DOLPHIN_INIT_DISPATCH_PROVEN_COUNT = 0
DOLPHIN_INIT_CALLER = UNKNOWN
DOLPHIN_INIT_CALLER_MODULE = UNKNOWN
DOLPHIN_INIT_CALL_SITE = UNKNOWN

DOLPHIN_INIT_ARGUMENT_PROVENANCE = UNKNOWN; client Init dispatch unresolved

DOLPHIN_INIT_IMMEDIATE_GATES = UNKNOWN; no proven client Init predecessor

DOLPHIN_SELECTION_VALUE_SOURCE = UNKNOWN
DOLPHIN_SELECTION_COMPARISON = UNKNOWN
DOLPHIN_SELECTION_TRUE_MEANS = UNKNOWN

DOLPHIN_ALTERNATIVE_BRANCH = UNKNOWN
PUFFER_ALTERNATIVE_EDGE = NOT_PROVEN

DOLPHIN_PUFFER_RELATION = UNKNOWN

DOLPHIN_BOOTSTRAP_OWNER_MODULE = UNKNOWN; libUE4.so is sole import candidate
DOLPHIN_BOOTSTRAP_OWNER_CLASS = UNKNOWN
DOLPHIN_BOOTSTRAP_OWNER_FUNCTION = UNKNOWN

CROSS_MODULE_EDGE = CreateDolphin import/export edge
FROM_MODULE = libUE4.so
TO_MODULE = libgcloud.so
EDGE_TYPE = ELF undefined dynamic symbol plus R_AARCH64_JUMP_SLOT
EDGE_CONFIDENCE = CONFIRMED / HIGH

DOLPHIN_SELECTION_PREDICATE_CLASS = UNKNOWN
DOLPHIN_SELECTION_PREDICATE_DESCRIPTION = Client Init dispatch and controlling value unresolved

DOLPHIN_SELECTION_APPDATA_DEPENDENCE = UNKNOWN
DOLPHIN_SELECTION_REMOTE_DEPENDENCE = UNKNOWN

PHASE15U_DOLPHIN_BRANCH_EXPLANATION = PARTIAL; branch observed, selector value not captured
PHASE16I_NON_DOLPHIN_BRANCH_EXPLANATION = PARTIAL; non-Dolphin observation confirmed, selector value not captured

DOLPHIN_TRIGGER_REPRODUCIBILITY = NOT_YET_DETERMINED

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

CURRENT_BLOCKER = CLIENT_DOLPHIN_INIT_DISPATCH_AND_SELECTOR_UNRESOLVED_AFTER_INVALIDATED_STATIC_IMPORT_CALL
NEXT_STEP = PC_ONLY_RESOLVE_LIBUE4_PLT_CALLSITE_REPRESENTATION_AND_CLIENT_DOLPHIN_DISPATCH
FINAL_GATE = E STATIC_AXIS_EXHAUSTED_RUNTIME_OR_STATE_COMPARISON_REQUIRED
COMMIT = Resolve client Dolphin bootstrap selection Phase16K
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Boundary

The analysis used exact local ELF metadata and existing read-only Ghidra
projects. No phone, ADB, Apex launch, active breakpoint, memory write, AppData
access, binary modification, network access, or Samsung access occurred. Raw
exports remain local-only and ignored.
