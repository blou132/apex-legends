# Evidence table

| Claim | Function | Address / site | Provenance | Edge type | Confidence |
|---|---|---|---|---|---|
| Constructor clears callback | `FUN_005bfb10` | G `0x005bfdd0`, ELF `0x004bfdd0` | `CActionMgr` constructor | `CONSTRUCTOR_ASSIGNMENT` | High |
| Non-null callback assignment | `FUN_005bc4bc` | G `0x005bc4e4`, ELF `0x004bc4e4` | manager `this`, non-null `x1` | `FIELD_WRITE` | High |
| Setter occupies manager slot `+0xe0` | `FUN_005bc4bc` | G `0x0097b0e0`, ELF `0x0087b0e0` | `CActionMgr` vtable | `VTABLE_SLOT_RESOLVED` | High |
| Strategy registers itself | `FUN_0050c4a8` | load/call G `0x0050c4ec`/`0x0050c4f4` | strategy `this` to manager | `INDIRECT_CALL_DATAFLOW_RESOLVED` | High |
| Version manager supplies strategy and client | `FUN_00576890` | load/call G `0x005769a0`/`0x005769a8` | fields `+0x10`, `+0x18`, `+0x28` | `INDIRECT_CALL_DATAFLOW_RESOLVED` | High |
| Callback is heap strategy object | `FUN_00576180 -> FUN_0050cf44` | G `0x005765d4` call | return stored at manager `+0x10` | `RETURN_VALUE_DATAFLOW` | High |
| Concrete callback hierarchy | RTTI | G `0x00997380`/`0x009973a0`/`0x009973b8` | exact RTTI inheritance | `RTTI` | High |
| Callback slot 0 implementation | `FUN_0050cb38` | vtable G `0x009789b0`, ELF `0x008789b0` | derived strategy vptr | `VTABLE_SLOT_RESOLVED` | High |
| Raw stage/error external forwarding | `FUN_0050cb38` | load/call G `0x0050cc04`/`0x0050cc0c` | external callback at strategy `+0x18` | `INDIRECT_CALL_DATAFLOW_RESOLVED` | High |
| Visible `I54140714` construction | unknown | none | no connected mapper/formatter | unresolved | Low |
