# Phase16A - CActionMgr external callback registration

Date: 2026-08-26

## Executive result

The exact `cu::CActionMgr+0x3b8` field-write search succeeds. The only proven
non-null writer is `FUN_005bc4bc`, reached through `CActionMgr` vtable slot
`+0xe0`. `CVersionMgrImp::CheckAppUpdate` supplies a heap
`cu::CVersionStrategy_Win32` object, which becomes the manager callback.

Callback slot `+0x00` resolves directly to `FUN_0050cb38`. It performs limited
bitfield checks for internal control, but forwards stage and the original raw
Dolphin error unchanged to a second external callback at
`CVersionStrategy+0x18`, slot `+0x28`. That external callback remains opaque;
no UE4, Lua, UI, error mapper, or `I54140714` formatter is proven.

## Required result

```text
CACTIONMGR_3B8_WRITE_CANDIDATES = FUN_005bfb10@0x005bfdd0 NULL; FUN_005bc4bc@0x005bc4e4 NON_NULL; FUN_005bff30@0x005bff78 NULL

CACTIONMGR_CALLBACK_ASSIGNMENT_FUNCTION = FUN_005bc4bc
CACTIONMGR_CALLBACK_ASSIGNMENT_SITE = Ghidra 0x005bc4e4, ELF 0x004bc4e4
CACTIONMGR_CALLBACK_VALUE_SOURCE = OWNER_FIELD: CVersionMgrImp+0x10 CVersionStrategy_Win32 this

CACTIONMGR_CALLBACK_REGISTRATION_FUNCTION = FUN_005bc4bc
CACTIONMGR_CALLBACK_REGISTRATION_SEMANTIC = OTHER: non-null callback setter

REGISTRATION_DIRECT_CALLERS = FUN_0050c4a8 virtual dispatch; FUN_00576890 strategy slot +0x48
CALLBACK_SUPPLIER_FUNCTION = FUN_00576890 CVersionMgrImp::CheckAppUpdate
CALLBACK_PROVENANCE = HEAP_OBJECT
CALLBACK_SOURCE_OBJECT = cu::CVersionStrategy_Win32
CALLBACK_SOURCE_FIELD = CVersionMgrImp+0x10
CALLBACK_SOURCE_FUNCTION = FUN_00576180 -> FUN_0050cf44 -> FUN_0050cedc

CALLBACK_CLASS = cu::CVersionStrategy_Win32
CALLBACK_INTERFACE = cu::IActionMgrCallback
CALLBACK_VTABLE_GHIDRA = 0x009789b0
CALLBACK_VTABLE_ELF = 0x008789b0

CALLBACK_SLOT_00_TARGET = FUN_0050cb38
CALLBACK_SLOT_00_THUNK = NO
CALLBACK_SLOT_00_IMPLEMENTATION = FUN_0050cb38

CALLBACK_STAGE_ARGUMENT = uint32 stage, forwarded unchanged
CALLBACK_ERROR_ARGUMENT = uint32 raw error, forwarded unchanged
DOLPHIN_CLIENT_ERROR_HANDLING = FORWARDED

STAGE_69_CLIENT_MEANING = UNKNOWN

CLIENT_VERSION_MANAGER_CLASS = CVersionMgrImp (source/monitor evidence)
CLIENT_VERSION_MANAGER_FUNCTION = FUN_00576180 Init; FUN_00576890 CheckAppUpdate

CLIENT_ERROR_MAPPER = NONE_IN_RESOLVED_CALLBACK
CLIENT_ERROR_MAPPER_INPUT = 0x0930002a
CLIENT_ERROR_MAPPER_OUTPUT = 0x0930002a
I54140714_CONSTRUCTION_CONFIRMED = NO

CALLBACK_BOUNDARY = EXTERNAL_CLIENT
LIBUE4_EXACT_CALLBACK_ANCHOR = NONE
LIBUE4_CALLBACK_CONSUMER = UNKNOWN

DOLPHIN_TO_UE4_EDGE = NO_NEW_EDGE
DOLPHIN_TO_LUA_EDGE = NO_NEW_EDGE

UPDATERESULT_EVENT_ROLE = REPORTING_ONLY
CACTIONMGR_STATIC_CALLBACK_AXIS_EXHAUSTED = NO

CURRENT_BLOCKER = UNRESOLVED_EXTERNAL_CLIENT_CALLBACK_AT_CVERSIONSTRATEGY_PLUS_0X18_SLOT_0X28
NEXT_RUNTIME_LAUNCH_REQUIRED = NO

FINAL_GATE = C CACTIONMGR_CALLBACK_SLOT_00_IMPLEMENTATION_RESOLVED
COMMIT = Resolve CActionMgr external callback Phase16A
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Boundary

No runtime, device, emulator, ADB, network, reimport, or full Ghidra analysis
occurred. Raw outputs and proprietary binaries remain local-only and ignored.
