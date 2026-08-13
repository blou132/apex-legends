# Lua package searcher

## Runtime result

No Lua loader call was observed because no client process ran. The package searcher between an extensionless module name and `FUN_049a8b54` remains `UNKNOWN`.

```text
LUA_PACKAGE_SEARCHER_OBSERVED = NO
INPUT_MODULE = UNKNOWN
TRANSFORMED_CANDIDATE = UNKNOWN
```

## Runtime address model

Phase3C established:

```text
ELF_VIRTUAL_OFFSET = GHIDRA_ADDRESS - 0x100000
RUNTIME_ADDRESS = LIBUE4_LOAD_BASE + ELF_VIRTUAL_OFFSET
```

Prepared offsets for a future compatible lab:

| Role | Ghidra address | ELF virtual offset |
| --- | --- | --- |
| Lua file loader | `0x49a8b54` | `0x48a8b54` |
| loader fallback helper | `0x49a9694` | `0x48a9694` |
| Lua chunk wrapper | `0x48ab4d0` | `0x47ab4d0` |

These are static address conversions only. They were not used as hooks and no runtime load base was obtained.
