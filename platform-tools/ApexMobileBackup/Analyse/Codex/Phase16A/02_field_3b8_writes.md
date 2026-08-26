# Exact field writes

The bounded executable-instruction scan found 161 instructions containing the
exact scalar `0x3b8`. Each store candidate was filtered by containing function,
object construction, vtable, and caller dataflow. Three writes are proven to
target `cu::CActionMgr+0x3b8`:

| Function | Ghidra site | Value | Role |
|---|---:|---|---|
| `FUN_005bfb10` | `0x005bfdd0` | null | constructor initialization |
| `FUN_005bc4bc` | `0x005bc4e4` | `x1` / non-null argument | callback assignment |
| `FUN_005bff30` | `0x005bff78` | null | destructor cleanup |

The non-null setter rejects a null argument and stores its second argument with
`str x1,[x0,#0x3b8]`.

Unrelated VFS/QTCVFS objects also use an offset `0x3b8`; for example,
`FUN_005db260` computes such an address on a different object layout. Those
candidates are rejected and are not promoted as `CActionMgr` writes.

```text
CACTIONMGR_3B8_WRITE_CANDIDATES = FUN_005bfb10@0x005bfdd0 NULL; FUN_005bc4bc@0x005bc4e4 NON_NULL; FUN_005bff30@0x005bff78 NULL
```
