# Evidence table

| Claim | Binary | Function | Ghidra | ELF | Reference site | Edge type | Confidence |
| --- | --- | --- | --- | --- | --- | --- | --- |
| `NormalConnectVersionSvr` owner | `libgcloud.so` | `FUN_00550ee4` | `0x00550ee4` | `0x00450ee4` | exact function/source strings | `CONTROL_FLOW` | high |
| `0x0930002a` creation | `libgcloud.so` | `FUN_00550ee4` | `0x005516e8` | `0x004516e8` | `mov`/`movk`, call at `0x005516f4` | `CONSTANT` | high |
| raw error relay | `libgcloud.so` | `FUN_00549800` | `0x00549800` | `0x00449800` | direct call from offline branch | `DIRECT_CALL` | high |
| action class and vtable | `libgcloud.so` | `dolphin::gcloud_version_action_imp` | `0x009797d0` | `0x008797d0` | RTTI and constructor assignment | `VTABLE_SLOT_RESOLVED` | high |
| manager `OnActionError` | `libgcloud.so` | `FUN_005bf94c` | `0x005bf94c` | `0x004bf94c` | `cu::CActionMgr` slot `+0x00` | `VTABLE_SLOT_RESOLVED` | high |
| error queue insertion | `libgcloud.so` | `FUN_005bf808` | `0x005bf808` | `0x004bf808` | direct call from `OnActionError` | `FIELD_ASSIGNMENT` | high |
| queued error consumption | `libgcloud.so` | `FUN_005be71c` | `0x005be71c` | `0x004be71c` | exact `ProcessActionError` string | `CONTROL_FLOW` | high |
| reporting adapter | `libgcloud.so` | `FUN_005bcce4` | `0x005bcce4` | `0x004bcce4` | `cu::CActionMgr` slot `+0xa8` | `VTABLE_SLOT_RESOLVED` | high |
| reporting field writer | `libgcloud.so` | `FUN_004d47cc` | `0x004d47cc` | `0x003d47cc` | writes `errcode` and `errmsg` | `DIRECT_CALL` | high |
| visible `I54140714` relation | runtime/static | unresolved | `UNKNOWN` | `UNKNOWN` | timing and suffix only | `RUNTIME_CORRELATION_ONLY` | low |
