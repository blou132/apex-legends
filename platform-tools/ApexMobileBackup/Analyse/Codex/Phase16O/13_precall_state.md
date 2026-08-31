# Precall producer state

The bounded 64-instruction window before `0x05a314cc` prepares path-like data,
but the entire final portion is post-noreturn:

- local helpers `FUN_0417a4ac` and `FUN_0417a69c` build or copy a bounded
  string/buffer representation;
- `x29 - 0x30` is passed to the first actual throw helper;
- unreachable cleanup may release a temporary pointer from `sp + 0xc0`;
- an optional level-5 log references the `UpdateInfoPath2` anchor;
- the call at `0x05a314c4` is physically `gettimeofday`, with registers shaped
  as `(output + 0x100, path, 0x100)`, which is incoherent for that import but
  coherent with the adjacent `strncpy` PLT entry;
- byte `output[0x1ff]` is zeroed immediately before `CreateDolphin`.

This is another instance of the same systematic one-physical-PLT-entry
mismatch already recorded in Phase16K/Phase16L. It does not restore
reachability or prove why these bytes were emitted.

```text
CREATEDOLPHIN_PRECALL_STATE = POST_NORETURN PATH; OUTPUT BUFFER +0x100; UPDATEINFOPATH2 STRING SLOT; DEBUG LOG SCRATCH; SYSTEMATIC PRECEDING-PLT MISMATCH
```
