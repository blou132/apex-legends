# Phase15Z - Dolphin update error mapping

Date: 2026-08-26

## Executive result

Static read-only analysis proves that `NormalConnectVersionSvr` is
`FUN_00550ee4`. Its network-unreachable branch constructs `0x0930002a` with an
AArch64 `mov`/`movk` pair and passes it unchanged to `FUN_00549800`.

The state path is resolved through `dolphin::gcloud_version_action_imp`,
`cu::CActionMgr::OnActionError`, the manager error queue, and
`ProcessActionError`. `ProcessActionError` forwards stage and raw code to an
external callback at `cu::CActionMgr+0x3b8`; its concrete slot target remains
unresolved.

The separate `UpdateResult` path is reporting-only. It formats the unchanged
raw code as unsigned decimal, writes `errcode` and `errmsg`, and commits the
event. No connected code subtracts `100000000`, extracts `54140714`, prepends
`I`, or renders a visible message. The timing/suffix correlation is therefore
still insufficient to prove construction of `I54140714`.

## Required result

```text
NORMAL_CONNECT_VERSION_SVR_FUNCTION = FUN_00550ee4
NORMAL_CONNECT_VERSION_SVR_GHIDRA = 0x00550ee4

DOLPHIN_RAW_ERROR_DECIMAL = 154140714
DOLPHIN_RAW_ERROR_HEX = 0x0930002a

DOLPHIN_0930002A_ANCHORS = FUN_00550ee4 Ghidra 0x005516e8-0x005516f4
DOLPHIN_ERROR_CREATOR = FUN_00550ee4 -> FUN_00549800
DOLPHIN_ERROR_CREATION_STYLE = BITFIELD

DOLPHIN_ERROR_DOMAIN_MASK = UNKNOWN
DOLPHIN_ERROR_MODULE_ID = UNKNOWN
DOLPHIN_ERROR_REASON_ID = 0x2a

PROCESS_ACTION_ERROR_FUNCTION = FUN_005be71c
PROCESSACTIONERROR_FORWARD_CHAIN = FUN_00550ee4 -> FUN_00549800 -> FUN_005bf94c -> FUN_005bf808 -> FUN_005be71c -> CActionMgr+0x3b8 slot +0x00 UNKNOWN
DOLPHIN_VERSION_MANAGER_CLASS = UNKNOWN
DOLPHIN_VERSION_MANAGER_VTABLE = UNKNOWN
DOLPHIN_VERSION_MANAGER_ERROR_CALLBACK = UNKNOWN

REMOVE_100M_TRANSFORM = NO
VISIBLE_ERROR_PREFIX_SOURCE = UNKNOWN
VISIBLE_ERROR_FORMATTER = UNKNOWN
ERROR_FORMAT_FUNCTION = FUN_005487ec (raw decimal reporting only)

UPDATERESULT_EVENT_OWNER = FUN_00549800 -> FUN_005bcce4 -> FUN_004d47cc
UPDATERESULT_EVENT_ROLE = REPORTING_ONLY

DOLPHIN_ERROR_FORWARDING_STYLE = RAW

LIBUE4_DOLPHIN_CLIENT_ANCHOR = NOT_SEARCHED_NO_CONCRETE_ANCHOR
LIBUE4_DOLPHIN_CONSUMER = UNKNOWN

I54140714_CONSTRUCTION_CONFIRMED = NO
I54140715_SAME_FORMAT_FAMILY = UNKNOWN

UPDATE_UI_OWNER = UNKNOWN
UPDATE_UI_DISPATCH_FUNCTION = UNKNOWN

UPDATE_STAGE_COUNT = NO_NEW_EVIDENCE
UPDATE_STAGE_INDEX = NO_NEW_EVIDENCE
UPDATE_STAGE_2_MEANING = NO_NEW_EVIDENCE

PUFFER_AND_DOLPHIN_RELATION = UNKNOWN

CURRENT_BLOCKER = UNRESOLVED_CACTIONMGR_EXTERNAL_CALLBACK_AND_NO_VISIBLE_FORMATTER
NEXT_RUNTIME_LAUNCH_REQUIRED = NO

FINAL_GATE = D DOLPHIN_ERROR_CREATION_AND_ACTION_CHAIN_RESOLVED
COMMIT = Resolve Dolphin update error mapping Phase15Z
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Boundary

No runtime, device, emulator, ADB, or network operation occurred. No
proprietary binary, raw decompilation, private identifier, or unrelated string
is published. Raw Ghidra output remains local-only and gitignored.
