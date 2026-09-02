# Owner `+0x1f8` relation

Prior lifecycle evidence contains three exact reads and one exact clear:

| ELF site | Operation |
|---|---|
| `0x04712cb4` | conditional Shutdown read |
| `0x04712ee8` | virtual cleanup slot `+0x08` |
| `0x05a2f094` | load into Init callback register `X5` |
| `0x04712f00` | clear after cleanup |

The provider ABI upgrades the field role to a callback pointer. No non-null
writer is present in those proven lifecycle functions.

```text
OWNER_PLUS_0X1F8_CALLBACK_RELATION = CALLBACK_POINTER_CONFIRMED
OWNER_PLUS_0X1F8_NON_NULL_WRITER = UNKNOWN_OPAQUE_PRELUDE
OWNER_PLUS_0X1F8_VALUE_SOURCE = UNKNOWN
```
