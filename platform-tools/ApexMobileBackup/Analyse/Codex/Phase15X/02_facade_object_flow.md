# Facade object flow

Ghidra resolves zero direct calling functions for `FUN_080d1ac8`. The only
references to the wrapper entry are:

| From | Reference type | Containing function |
| ---: | --- | --- |
| `0x02aa1af0` | `INDIRECTION` | none |
| `0x0347c4e8` | `DATA` | none |

A targeted read of those exact cells finds no xref to either cell and no
function owner. They are consistent with indirect Unreal dispatch/registration
metadata, but no stronger semantic classification is promoted.

The returned `GCloudPufferImp` pointer therefore crosses a dynamic dispatch
boundary. Static object flow does not establish a store, owner field, global
singleton, smart pointer, reader, or subsequent call on the same pointer.

```text
CREATEPUFFER_DIRECT_CALLERS = NONE_STATICALLY_RESOLVED
PUFFER_FACADE_STORAGE = UNKNOWN_AFTER_INDIRECT_RETURN
PUFFER_FACADE_FIELD_OFFSET = UNKNOWN
PUFFER_FACADE_OWNER_OBJECT = UNKNOWN
PUFFER_FACADE_FIRST_OWNER_FUNCTION = UNKNOWN
```
