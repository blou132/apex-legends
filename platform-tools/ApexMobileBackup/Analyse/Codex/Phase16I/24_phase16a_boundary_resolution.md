# Phase16A boundary resolution

Phase16I strengthened the static half of the Phase16A chain:

```text
CVersionMgrImp::Init
  -> external callback container in X1
  -> callback object from *X1
  -> CVersionMgrImp+0x18
  -> CVersionStrategy+0x18
  -> FUN_0050cb38
  -> callback vtable slot +0x28
  -> UNKNOWN
```

The runtime target was not reached in the single authorized window, so the
external callback boundary remains unresolved.

```text
PHASE16A_EXTERNAL_CALLBACK_BOUNDARY_RESOLVED = NO
```
