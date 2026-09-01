# Owner `+0x1f8`

Three exact readers and one exact writer are present in proven owner methods.

| Ghidra | ELF | Operation |
|---|---|---|
| `0x04812cb4` | `0x04712cb4` | conditional read during shutdown |
| `0x04812ee8` | `0x04712ee8` | load, vtable slot `+0x08` cleanup |
| `0x05b2f094` | `0x05a2f094` | pair load into Init argument `X5` |
| `0x04812f00` | `0x04712f00` | clear after cleanup |

The value behaves like an adjacent nullable interface object: it supports a
virtual cleanup call and is passed alongside the primary interface. Its exact
type and acquisition remain unknown, so `RELATED_INTERFACE` is probable, not
confirmed.

```text
OWNER_PLUS_0X1F8_READER_COUNT = 3
OWNER_PLUS_0X1F8_WRITER_COUNT = 1_PROVEN_CLEAR
OWNER_PLUS_0X1F8_ROLE = RELATED_INTERFACE_PROBABLE
```
