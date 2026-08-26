# Phase15W - Puffer dynamic callback consumer

Date: 2026-08-26

## Executive result

Bounded read-only Ghidra analysis resolves the dynamic callback that stopped
Phase15V. `CPufferInitActionResult::ProcessResult` receives its callback as its
second argument. `CPufferActionCallBackImp` stores this pointer at `+0x08` and
passes it to the result while draining the result queue.

The pointer is the `cu::IPufferCallBack` secondary subobject at `this+0x08` of
the concrete `GCloud::GCloudPufferImp` created by `CreatePuffer`. Its secondary
vtable is Ghidra `0x009785f8`, ELF `0x008785f8`. Slot `+0x10` resolves to
`FUN_00503050`, which adjusts the secondary base and reaches `FUN_00503024`.

`FUN_00503024` is a raw boundary forwarder: it loads a downstream client
callback from `GCloudPufferImp+0x18` and calls that object's slot `+0x10` with
the success flag and Puffer code unchanged. The downstream client callback is
externally supplied to `GCloudPufferImp::Init`, but its concrete `libUE4`
registration/constructor is not resolved.

The exact `libUE4` search finds only a `CreatePuffer` facade-factory call at
`FUN_080d1ac8:0x080d1b84`; it finds no exact `CreatePufferCallBack` anchor. The
five exact Puffer values `0x0430002e`-`0x04300032` each have zero instruction or
defined-data hits in `libUE4`. No client mapper, update manager, UI dispatch,
Lua edge, or exact `I54140714` construction can be promoted.

## Required result

```text
PROCESSRESULT_CALLBACK_OBJECT = cu::IPufferCallBack subobject of GCloud::GCloudPufferImp
PROCESSRESULT_CALLBACK_FIELD_OFFSET = CPufferActionCallBackImp+0x08; callback is not stored in result
CALLBACK_OBJECT_SOURCE = GCloudPufferImp this+0x08 -> config[0] -> CPufferActionCallBackImp+0x08

CALLBACK_INTERFACE_NAME = cu::IPufferCallBack
CALLBACK_CONCRETE_CLASS = GCloud::GCloudPufferImp

CALLBACK_VTABLE_ADDRESS_GHIDRA = 0x009785f8
CALLBACK_SLOT_10_TARGET = FUN_00503050 -> FUN_00503024
SUBMIT_SLOT_20_TARGET = FUN_004f3a14
SAME_MANAGER_OBJECT = NO

CALLBACK_REGISTRATION_FUNCTION = FUN_0050323c -> FUN_004f02dc
CALLBACK_REGISTERED_EXTERNALLY = NO

LIBUE4_CALLBACK_REGISTRATION_SITE = UNKNOWN
LIBUE4_CALLBACK_OBJECT_CONSTRUCTOR = UNKNOWN

PUFFER_0430002E_LIBUE4_HITS = 0
PUFFER_0430002F_LIBUE4_HITS = 0
PUFFER_04300030_LIBUE4_HITS = 0
PUFFER_04300031_LIBUE4_HITS = 0
PUFFER_04300032_LIBUE4_HITS = 0

PUFFER_ERROR_FAMILY_MASKING = NO_IN_RESOLVED_FORWARDER

CLIENT_PUFFER_CONSUMER = UNKNOWN_BEYOND_GCLOUDPUFFERIMP_FORWARDER
CLIENT_ERROR_MAPPING_FUNCTION = UNKNOWN
CLIENT_ERROR_MAPPING_STYLE = UNKNOWN; libgcloud boundary is RAW_PASS_THROUGH

I54140714_CONSTRUCTION_CONFIRMED = NO

UPDATE_MANAGER_FUNCTION = UNKNOWN
UPDATE_UI_DISPATCH_FUNCTION = UNKNOWN
UPDATE_UI_OWNER = UNKNOWN

PUFFER_TO_LUA_DIRECT_EDGE = NO

CURRENT_BLOCKER = OPAQUE_DYNAMIC_CALLBACK_BOUNDARY_AT_GCLOUDPUFFERIMP_CLIENT_CALLBACK
NEXT_RUNTIME_LAUNCH_REQUIRED = NO

FINAL_GATE = D CONCRETE_DYNAMIC_CALLBACK_IMPLEMENTATION_RESOLVED
COMMIT = Resolve Puffer dynamic callback consumer Phase15W
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

No device, emulator, ADB, runtime launch, network access, response emulation,
patch, bypass, hook, reimport, or full analysis was used. Raw Ghidra output is
local-only and gitignored. No proprietary binary or bulk raw output is added to
the repository.
