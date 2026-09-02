# Client argument mapping

The real client call at ELF `0x05a2f0ac` dispatches slot `+0x10` from the
interface in `[DolphinUpdater+0x1f0]`. Its exact register setup is:

| Register | Client source |
|---|---|
| `X0` | `[DolphinUpdater+0x1f0]` |
| `X1` | `SP+0x680` |
| `X2` | `SP+0x280` |
| `X3` | `X21`, inherited from the opaque prefix |
| `X4` | mode-dependent; null on the observed path |
| `X5` | `[DolphinUpdater+0x1f8]` |

Mapping provider `X5` directly to this call proves the client callback source.

```text
CLIENT_INIT_CALLSITE_VALID = YES
CLIENT_CALLBACK_ARGUMENT_SOURCE = OWNER_PLUS_0X1F8
CALLBACK_OBJECT_BASE_SOURCE = [DOLPHINUPDATER_PLUS_0X1F8]
```
