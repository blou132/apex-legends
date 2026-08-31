# Real CreateDolphin callers

Exactly one executable direct branch reaches the proven `CreateDolphin` stub:

| ELF callsite | Opcode | Containing FDE | Path | Classification |
|---:|---:|---|---|---|
| `0x05a314cc` | `0x95264109` | `0x05a311a8...0x05a31ca8` | Direct to `0x0a3c18f0` | `ERROR_HELPER_PATH` |

The classification reflects the return consumer, not the factory call
identity. Immediately after the call, the pointer is copied to `x1`; the next
exact branch invokes imported `std::__throw_length_error(char const*)`, whose
ABI consumes `x0` and does not retain `x1`. No normal owner path continues
with the returned object.

The machine path is real, but it is not a factory-owner candidate.

```text
REAL_CREATEDOLPHIN_CALLSITE_COUNT = 1
REAL_CREATEDOLPHIN_CALLSITE_CLASSIFICATION = ERROR_HELPER_PATH
```
