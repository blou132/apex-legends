# Same-collection filter

The 24 lookup users converge on the source registry at parent `+0xf8`.
Neither the exact class accessor nor the direct class wrapper exposes an
insert, remove, size increment, or value write for that registry.

The derived cache at wrapper `+0x60` is not the same logical container despite
using the same class key.

```text
SAME_COLLECTION_MUTATION_COUNT = 0
SAME_COLLECTION_PROVENANCE = LOOKUP_ONLY
```
