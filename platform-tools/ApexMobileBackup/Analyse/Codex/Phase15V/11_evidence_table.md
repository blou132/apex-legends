# Evidence table

| Claim | Binary | Ghidra address | ELF VA / file offset | Function | Evidence type | Confidence |
| --- | --- | ---: | --- | --- | --- | --- |
| Puffer owner is `libgcloud.so` | `libgcloud.so` | `0x79aef0` | `0x69aef0` / `0x69aef0` | function-name data | `STRING_XREF` | `CONFIRMED` |
| `MakeSureGetUrlFromServer` implementation | `libgcloud.so` | `0x4ffd44` | `0x3ffd44` / `0x3ffd44` | `FUN_004ffd44` | `STRING_XREF`, `DECOMPILATION` | `CONFIRMED` |
| runner calls request coordinator | `libgcloud.so` | `0x501e94` | `0x401e94` / `0x401e94` | `FUN_00501d0c` | `DIRECT_CALL` | `CONFIRMED` |
| network failures map to internal `0x043000xx` codes | `libgcloud.so` | `0x4ffd44` | `0x3ffd44` / `0x3ffd44` | `FUN_004ffd44` | `CONTROL_FLOW`, `CONSTANT` | `CONFIRMED` |
| response callback is registered | `libgcloud.so` | `0x4fd278` | `0x3fd278` / `0x3fd278` | `FUN_004fd278` -> `FUN_004fc7c0` | `TABLE_REFERENCE`, `VIRTUAL_CALL` | `CONFIRMED` |
| runner submits failure result | `libgcloud.so` | `0x501f04` | `0x401f04` / `0x401f04` | `FUN_00501d0c` -> `FUN_004fcaf4` | `DIRECT_CALL` | `CONFIRMED` |
| result object stores internal code | `libgcloud.so` | `0x4ee318` | `0x3ee318` / `0x3ee318` | `FUN_004ee318` | `DIRECT_CALL`, `DECOMPILATION` | `CONFIRMED` |
| result processing forwards to dynamic manager | `libgcloud.so` | `0x4ee1dc` | `0x3ee1dc` / `0x3ee1dc` | `CPufferInitActionResult::ProcessResult` | `TABLE_REFERENCE`, `VIRTUAL_CALL` | `CONFIRMED` |
| `ProcessResult` vtable entry | `libgcloud.so` | `0x977c80` | `0x877c80` / `0x867c80` | data -> `FUN_004ee1dc` | `TABLE_REFERENCE` | `CONFIRMED` |
| `NetworkNotReachable` exists below Puffer | `libgcloud.so` | `0x3e9860` | `0x2e9860` / `0x2e9860` | `FUN_003e9860` | `STRING_XREF` | `CONFIRMED`; Puffer edge `UNKNOWN` |
| rendered `I54140714` coincides with Puffer failure | runtime evidence | n/a | n/a | n/a | `RUNTIME_CORRELATION_ONLY` | `CONFIRMED` timing, no ownership |
| UI owner is unresolved | client boundary | n/a | n/a | dynamic callback | `VIRTUAL_CALL` | `UNKNOWN` |

Absence checks for exact integers and strings are documented in
`05_error_code_search.md`; absence is not represented as a positive ownership
claim in this table.
