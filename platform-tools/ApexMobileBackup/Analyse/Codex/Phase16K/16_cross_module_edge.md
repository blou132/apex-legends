# Cross-module edge

The exact cross-module symbol edge is proven independently of the unresolved
semantic owner:

```text
CROSS_MODULE_EDGE = CreateDolphin import/export edge
FROM_MODULE = libUE4.so
TO_MODULE = libgcloud.so
EDGE_TYPE = ELF undefined dynamic symbol plus R_AARCH64_JUMP_SLOT
EDGE_CONFIDENCE = CONFIRMED / HIGH
```

The `libUE4.so` Ghidra executable hash matches the exact preserved ELF, and
the local image-base correlation was revalidated before interpreting the
targeted callsite.
