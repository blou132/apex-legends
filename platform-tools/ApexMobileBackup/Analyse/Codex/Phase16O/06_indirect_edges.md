# Indirect edges

The exact function range contains 14 `BLR` instructions and no `BR`
instruction. Each `BLR` is modeled as an indirect callee followed by its local
continuation; none is a computed continuation or switch dispatch.

Because there is no `BR`, no `ADRP`/`LDRSW`/`ADD`/`BR` jump-table shape exists
in the function. Ghidra also reports no incoming reference to the target block
or its first post-throw fallthrough.

```text
INDIRECT_EDGE_TO_CREATEDOLPHIN_BLOCK = NO
JUMP_TABLE_PRESENT = NO
JUMP_TABLE_TARGETS_CREATEDOLPHIN_BLOCK = NO
```
