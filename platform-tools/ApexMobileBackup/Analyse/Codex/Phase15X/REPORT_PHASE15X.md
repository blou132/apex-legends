# Phase15X - CreatePuffer client object flow

Date: 2026-08-26

## Executive result

Bounded read-only Ghidra analysis confirms that `FUN_080d1ac8` is a facade
factory wrapper. At `0x080d1b84` it restores its frame and tail-branches to the
imported `CreatePuffer` thunk. The `GCloudPufferImp` pointer returned in `x0` is
therefore returned directly to the wrapper's caller; it is not stored or used
again inside the wrapper.

The exact caller trace resolves zero calling functions at both permitted caller
levels. The wrapper entry has only an `INDIRECTION` reference from
`0x02aa1af0` and a `DATA` reference from `0x0347c4e8`; neither address belongs
to a function or has an xref reader. These are an unresolved indirect Unreal
dispatch boundary, not a statically traceable consumer.

Because the returned facade cannot be tied to a store or same-pointer Init
call, Phase15X cannot resolve the client value passed in `x2` to
`GCloudPufferImp::Init`, its constructor/vtable, or callback slot `+0x10`.
There is consequently no new update-manager, UE4 event, Lua, UI, progress, or
`I54140714` edge. The mandated stop rule applies and this static Puffer client
object-flow axis is exhausted.

## Required result

```text
CREATEPUFFER_WRAPPER = FUN_080d1ac8 at 0x080d1ac8
CREATEPUFFER_CALL_SITE = 0x080d1b84

PUFFER_FACADE_STORAGE = UNKNOWN_AFTER_INDIRECT_RETURN
PUFFER_FACADE_OWNER_OBJECT = UNKNOWN

PUFFER_INIT_CALLER_FUNCTION = UNKNOWN
PUFFER_INIT_CALL_SITE = UNKNOWN
PUFFER_INIT_VTABLE_SLOT = +0x10 (semantic known; invocation unresolved)

PUFFER_INIT_CLIENT_CALLBACK_ARGUMENT_REGISTER = x2

CLIENT_CALLBACK_PROVENANCE = UNKNOWN
CLIENT_CALLBACK_SOURCE_FUNCTION = UNKNOWN
CLIENT_CALLBACK_SOURCE_FIELD = UNKNOWN

CLIENT_CALLBACK_INTERFACE_NAME = UNKNOWN
CLIENT_CALLBACK_CONCRETE_CLASS = UNKNOWN
CLIENT_CALLBACK_CONSTRUCTOR = UNKNOWN
CLIENT_CALLBACK_VTABLE = UNKNOWN

CLIENT_CALLBACK_SLOT_10_TARGET = UNKNOWN
CLIENT_CALLBACK_SUCCESS_HANDLING = UNKNOWN
CLIENT_CALLBACK_ERROR_HANDLING = UNKNOWN
CLIENT_ERROR_TRANSFORMATION = UNKNOWN

CLIENT_SECONDARY_CALLBACK_TARGET = UNKNOWN

CLIENT_UPDATE_MANAGER = UNKNOWN
CLIENT_UPDATE_MANAGER_FUNCTION = UNKNOWN

PUFFER_TO_UE4_EVENT_EDGE = NO
PUFFER_TO_LUA_DIRECT_EDGE = NO_NEW_EDGE

UPDATE_UI_OWNER = UNKNOWN
UPDATE_UI_DISPATCH_FUNCTION = UNKNOWN

I54140714_CONSTRUCTION_CONFIRMED = NO
UPDATE_STAGE_2_NEW_EVIDENCE = NO

STATIC_PUFFER_CLIENT_BOUNDARY_EXHAUSTED = YES

CURRENT_BLOCKER = CREATEPUFFER_RETURN_CONSUMER_HIDDEN_BEHIND_UNRESOLVED_INDIRECT_DISPATCH
NEXT_RUNTIME_LAUNCH_REQUIRED = NO

FINAL_GATE = G CREATEPUFFER_OBJECT_FLOW_OPAQUE
COMMIT = Trace Puffer client callback registration Phase15X
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

No device, emulator, ADB, client launch, network operation, hook, debugger,
reimport, patch, bypass, global vtable scan, or proprietary/raw output was used
or committed. Raw exports remain local-only and gitignored.
