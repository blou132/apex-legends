# Evidence table

| Claim | Module/site | Edge type | Classification | Reason |
|---|---|---|---|---|
| Exact factory identity | `libgcloud.so` ELF `0x0044514c` | Dynamic export and bounded code | CONFIRMED | Global function, size 40, allocates and constructs the known object |
| `GCloudDolphinImp` construction | Constructor helper / vptr `0x00979620` | Direct store | CONFIRMED | Primary and secondary interface vptrs installed |
| Client module imports factory | `libUE4.so` relocation `0x0b37e8c8` | `R_AARCH64_JUMP_SLOT` | CONFIRMED | Exact undefined dynsym and relocation |
| Other native modules import factory | Exact preserved native set | ELF metadata | INVALIDATED | No exact undefined symbol or relocation |
| Direct client code caller | `FUN_05b311a8`, Ghidra `0x05b314cc` | Direct PLT call | CONFIRMED syntactically | Exact call instruction exists |
| Direct caller is bootstrap owner | Same block | ABI/dataflow | INVALIDATED | Adjacent-PLT pattern; return is not retained or dispatched |
| Upper callback identity | `FUN_080f87d0` | Embedded source/function identifiers | CONFIRMED | `OnDolphinFirstExtractSuccess` post-extract callback |
| Semantic factory registration | Provider/consumer bounded refs | Table/data flow | UNKNOWN | No registry entry or consumer found |
| Returned-object owner/storage | Client forward slice | Store/data flow | UNKNOWN | No valid store follows the exact factory call |
| Client Init dispatch | Required vptr/slot provenance | Virtual call | UNKNOWN | Zero provenance-qualified callsites |
| Dolphin selection predicate | Client predecessor/backward slice | Control flow | UNKNOWN | No proven Init caller from which to slice |
| Puffer is alternative | Existing Puffer anchors | Branch edge | UNKNOWN | No exact shared selector or direct alternative edge |
| Historical Thread-10 executor | Phase15U/Phase16J evidence | Runtime log correlation | CONFIRMED | Previously resolved at HIGH confidence |
