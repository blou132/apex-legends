# Phase7 - Native Lua loader

## Reconstructed pipeline

```text
module path
  -> FUN_049a8b54 path construction and lookup
  -> virtual path resolver FUN_082693bc
  -> optional byte provider or fallback FUN_049a9694
  -> generic file manager FUN_046355e8
  -> backend open thunk_FUN_049825bc
  -> archive/file object size + read + status
  -> optional UTF-8 BOM removal
  -> FUN_048ab4d0 chunk load/compile handoff
  -> FUN_066365bc error/closure helper on failure
```

## Functions

| Role | Ghidra VA | ELF VA | Semantic parameters | Status |
| --- | --- | --- | --- | --- |
| Module/file loader `FUN_049a8b54` | `0x49a8b54` | `0x48a8b54` | Lua state/context, module-path `FString`, chunk/context name, flags; exact stripped prototype unknown | **CONFIRMED semantics** |
| Fallback file read `FUN_049a9694` | `0x49a9694` | `0x48a9694` | virtual path plus destination byte buffer; exact stripped prototype unknown | **CONFIRMED semantics** |
| Virtual path resolver `FUN_082693bc` | `0x82693bc` | `0x81693bc` | file-manager object and candidate path; delegates through slot `+0x70` | **CONFIRMED delegation** |
| File-manager factory `FUN_046355e8` | `0x46355e8` | `0x45355e8` | no proven caller argument; returns singleton at `0xb7cf9b0` | **CONFIRMED singleton** |
| Backend open `thunk_FUN_049825bc` | `0x49825b8` | `0x48825b8` | backend object, normalized path, open/read flags; exact stripped prototype unknown | **CONFIRMED generic open role** |
| Chunk handoff `FUN_048ab4d0` | `0x48ab4d0` | `0x47ab4d0` | Lua state, buffer callback `FUN_0a02e2a4`, buffer context, chunk/module name | **PROBABLE Lua load/compile** |
| Error/closure helper `FUN_066365bc` | `0x66365bc` | `0x65365bc` | Lua state/context and error/closure data; exact stripped prototype unknown | **CONFIRMED helper role** |

`FUN_049a8b54` combines runtime prefixes with the requested module path, tests the candidate through the virtual file layer, reads bytes through either an optional provider or `FUN_049a9694`, skips `EF BB BF` when present, and passes the unchanged remaining bytes to `FUN_048ab4d0`. The failure path includes the `LoadFile` diagnostic and calls `FUN_066365bc`.

No decompression or custom decode is visible between the returned byte buffer and the chunk handoff. Such processing, if any, must occur below the generic provider/reader boundary. Stripped symbols prevent assigning exact `lua_load`/`luaL_loadbuffer` names; `FUN_048ab4d0` is therefore not promoted beyond **PROBABLE**.
