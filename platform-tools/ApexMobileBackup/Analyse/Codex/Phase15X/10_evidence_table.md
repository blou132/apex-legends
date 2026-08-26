# Evidence table

| Claim | Source | Source address | Reference/call site | Target | Target address | Edge type |
| --- | --- | ---: | ---: | --- | ---: | --- |
| Wrapper reaches facade factory | `FUN_080d1ac8` | `0x080d1ac8` | `0x080d1b84` | imported `CreatePuffer` | `0x0a4c1730` | `DIRECT_CALL` |
| Factory result is returned directly | `FUN_080d1ac8` | `0x080d1ac8` | `0x080d1b74`-`0x080d1b84` | unresolved indirect caller | `UNKNOWN` | `RETURN_VALUE_DATAFLOW` |
| Wrapper has indirection reference | wrapper entry | `0x080d1ac8` | `0x02aa1af0` | metadata cell, no function owner | `0x02aa1af0` | not promoted (`INDIRECTION`) |
| Wrapper has data reference | wrapper entry | `0x080d1ac8` | `0x0347c4e8` | metadata cell, no function owner | `0x0347c4e8` | not promoted (`DATA`) |
| Known facade Init semantic | `GCloudPufferImp` primary vtable | `0x009784c0` (`libgcloud`) | slot `+0x10` | `FUN_0050323c` | `0x0050323c` | `VTABLE_SLOT_RESOLVED` |
| Known Init callback destination | `FUN_0050323c` | `0x0050323c` | `0x005032e0` | `GCloudPufferImp+0x18` | n/a | `FIELD_ASSIGNMENT` |

Ghidra reports zero calling functions for `FUN_080d1ac8`. Neither exact
metadata cell has a containing function or an xref reader. Therefore no edge
from the returned `x0` value to a facade store, Init call, callback source, or
client callback vtable is promoted.
