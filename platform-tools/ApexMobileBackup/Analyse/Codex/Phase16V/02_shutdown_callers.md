# Shutdown callers

The exact target scanner found one branch to Shutdown. It is the already-known
adjustor thunk itself:

| Source ELF | Target ELF | Kind | Function |
|---|---|---|---|
| `0x04712c68` | `0x04712c6c` | `B` | `FUN_04812c64` |

No external direct branch targets the thunk at ELF `0x04712c64`. Therefore the
top-level caller that supplies the secondary `this` remains unknown.

```text
SHUTDOWN_DIRECT_BRANCH_COUNT = 1
SHUTDOWN_THUNK_BRANCH_COUNT = 0
```
