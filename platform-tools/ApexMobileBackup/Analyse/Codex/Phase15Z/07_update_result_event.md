# ClientEvent and UpdateResult

`FUN_00549800` separates state handling from reporting:

1. `cu::CActionMgr` slot `+0x00` reaches `FUN_005bf94c` (`OnActionError`) and
   queues the action with the raw code.
2. The action event name is copied by `FUN_00504b18`; runtime identifies that
   value as `UpdateResult`.
3. `FUN_005487ec` converts the unchanged code to unsigned decimal text.
4. `cu::CActionMgr` slot `+0xa8` resolves to `FUN_005bcce4`.
5. `FUN_005bcce4` calls `FUN_004d47cc`, which populates `errcode` and `errmsg`
   fields and commits/sends the event through a reporting interface.

This proves that the `UpdateResult` branch is reporting/telemetry, not the UI
dispatch. The exact concrete owner behind the runtime label `GemReportHelper`
is not named by static RTTI in the preserved binary.

UPDATERESULT_EVENT_OWNER = FUN_00549800 -> FUN_005bcce4 -> FUN_004d47cc
UPDATERESULT_EVENT_ROLE = REPORTING_ONLY
GEM_REPORT_HELPER_STATIC_OWNER = UNKNOWN
