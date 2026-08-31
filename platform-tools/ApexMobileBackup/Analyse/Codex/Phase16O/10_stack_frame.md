# Stack frame

The prologue reserves `0x1f0` bytes and sets `x29 = sp + 0x190`.

| Location | Concrete offset | Bounded role |
|---|---:|---|
| `x29 - 0x30` | `sp + 0x160` | Pointer slot prepared as the first throw argument; not initialized by the actual noreturn helper |
| `x29 - 0x38` | `sp + 0x158` | Pointer slot prepared after `CreateDolphin`; uninitialized on any actual path |
| `sp + 0x40` | `sp + 0x40` | Reused temporary scratch; optional logging stores a context pointer here |

The decompiler renders `x29 - 0x30` and `x29 - 0x38` as string-like pointer
locals because later unreachable code reads them that way. The actual imported
throw helpers do not construct those locals. `sp + 0x40` is reused by multiple
operations and cannot be promoted to a stable object type.

```text
LOCAL_X29_MINUS_38_ROLE = POINTER_SLOT_UNINITIALIZED_ON_ACTUAL_PATH
LOCAL_SP_PLUS_40_ROLE = TEMPORARY_SCRATCH
```
