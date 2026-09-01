# Init arguments

The exact register setup immediately before the slot `+0x10` call is:

| Register | Proven source |
|---|---|
| `X0` | `[DolphinUpdater + 0x1f0]` interface |
| `X1` | stack local at `SP+0x680` |
| `X2` | stack local at `SP+0x280` |
| `X3` | inherited local held in `X21` |
| `X4` | null |
| `X5` | adjacent owner field `[DolphinUpdater + 0x1f8]` |

The protected prelude prevents safe semantic labels for the stack locals and
`X21`; they are not guessed as callback, configuration, path, or update type.

```text
REAL_DOLPHIN_INIT_ARGUMENT_PROVENANCE = X0_OWNER+0x1f0; X1_SP+0x680; X2_SP+0x280; X3_X21_OPAQUE; X4_NULL; X5_OWNER+0x1f8
```
