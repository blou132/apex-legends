# Phase15V - Puffer update failure ownership

Date: 2026-08-25

## Executive result

Bounded static analysis confirms that `libgcloud.so` owns
`CPufferInitAction::MakeSureGetUrlFromServer`. The implementation is
`FUN_004ffd44` at Ghidra `0x004ffd44`, ELF VA/file offset `0x003ffd44`. It is a
coordinator for `PufferUpdateService.GetUpdateInfo`, not merely a log wrapper:
it initializes the RPC path, waits for connection, starts version retrieval,
receives a callback, and extracts primary/spare update URLs.

The request coordinator directly classifies RPC setup, timeout, connection,
callback, and cancellation failures into Puffer-internal codes
`0x0430002e`-`0x04300032`. `CPufferInitAction::run` submits the result as a
`CPufferInitActionResult`; its vtable leads to `ProcessResult`, which forwards
the status through a dynamic manager callback. That is the proven boundary.

Neither visible code `I54140714` nor historical `I54140715` exists as a string,
32-bit integer, or 64-bit integer in the bounded relevant native checks. No
connected formatter or mapping table converts Puffer's internal code to the
visible value. The UI owner and progress state `2/17` also remain unknown. The
runtime coincidence is not promoted to a direct ownership edge.

## Required result

```text
PUFFER_OWNER_LIBRARY = libgcloud.so
PUFFER_FUNCTION_ADDRESS_GHIDRA = 0x004ffd44
PUFFER_FUNCTION_ADDRESS_ELF = 0x003ffd44

PUFFER_FAILURE_FUNCTIONS = FUN_004ffd44, FUN_00501d0c, FUN_004fc7c0, FUN_004fcaf4, FUN_004ee1dc
PUFFER_FAILURE_CALLBACK_CHAIN = CPufferInitAction::run -> MakeSureGetUrlFromServer -> CPufferInitActionResult -> ProcessResult -> dynamic manager callback

54140714_INTEGER_ANCHOR = NOT_FOUND
54140715_INTEGER_ANCHOR = NOT_FOUND

ERROR_CODE_CONSTRUCTION_STYLE = DYNAMIC_UNKNOWN
ERROR_MAPPING_FUNCTION = UNKNOWN
NETWORK_FAILURE_TO_CLIENT_ERROR_MAPPING = UNKNOWN

I54140714_CONSTRUCTION_CONFIRMED = NO
I54140714_PUFFER_DIRECT_LINK = NO_DIRECT_STATIC_EDGE
I54140715_VARIANT_RELATION = UNKNOWN

UPDATE_UI_OWNER = UNKNOWN
UPDATE_UI_OWNER_CONFIDENCE = LOW

UPDATE_STAGE_COUNT_17_STATIC = UNKNOWN
UPDATE_STAGE_2_OWNER = UNKNOWN

PUFFER_REQUEST_COORDINATOR = FUN_004ffd44 / CPufferInitAction::MakeSureGetUrlFromServer
PUFFER_TRANSPORT_FUNCTION = FUN_004ed038 PufferUpdateService.GetUpdateInfo (generated path; coordinator edge dynamic)
PUFFER_CALLBACK_FUNCTION = FUN_004fc7c0 / ResUpdateCallBack
PUFFER_HOST_SOURCE = RUNTIME_CONFIG

LUA_DIRECT_EDGE_FROM_UPDATE = NO
LOGIN_DIRECT_EDGE_FROM_UPDATE = NO

CURRENT_BLOCKER = DYNAMIC_MANAGER_CALLBACK_PREVENTS_CLIENT_ERROR_AND_UI_ATTRIBUTION
NEXT_RUNTIME_LAUNCH_REQUIRED = NO

FINAL_GATE = D PUFFER_FAILURE_CALLBACK_CHAIN_RESOLVED_UI_LINK_UNKNOWN
COMMIT = Resolve Puffer update error ownership Phase15V
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

No phone or emulator was used and Apex was not launched. No network request,
host contact, response, credential, private value, proprietary binary, or bulk
decompilation is published. Raw targeted Ghidra output remains local-only and
gitignored.
