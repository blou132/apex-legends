# Owner flag `+0x45`

The field is read at ELF `0x05a2f088`; nonzero branches away from the Init
path. Three bounded writes are proven:

| Ghidra | ELF | Value | Path |
|---|---|---:|---|
| `0x080f6f40` | `0x07ff6f40` | 0 | first-extract-success owner continuation |
| `0x05b2f808` | `0x05a2f808` | 1 | `CheckUpdate` |
| `0x05b2fbd4` | `0x05a2fbd4` | 1 | `CheckUpdate`; `W26=1` at ELF `0x05a2fa3c` |

This establishes persistent state behavior but not a unique high-level name.
Its initial value remains unknown because the owner initializer is unresolved.

```text
OWNER_PLUS_0X45_ROLE = PERSISTENT_CHECKUPDATE_STATE_FLAG
OWNER_PLUS_0X45_WRITE_SOURCES = ELF_0x07ff6f40_ZERO; ELF_0x05a2f808_ONE; ELF_0x05a2fbd4_ONE
```
