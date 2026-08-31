# Function-local CFG

The bounded extractor decoded only `0x05a311a8...0x05a31ca8`. Blocks split at
direct calls, indirect calls, conditional and unconditional control transfers,
and returns. Direct calls normally contribute a fallthrough edge; known
noreturn imports terminate the semantic graph.

| Measure | Result |
|---|---:|
| Instructions | 704 |
| Basic blocks | 206 |
| Raw direct edges | 283 |
| Noreturn-aware direct edges | 278 |
| Indirect calls (`BLR`) | 14 |
| Indirect branches (`BR`) | 0 |
| Raw entry-reachable blocks | 206 |
| Semantic entry-reachable blocks | 54 |

The indirect count reports 14 call edges and zero computed continuation
branches. No `BL` callee was followed while building this CFG.

```text
FUNCTION_BASIC_BLOCK_COUNT = 206
FUNCTION_DIRECT_EDGE_COUNT = 283_RAW / 278_NORETURN_AWARE
FUNCTION_INDIRECT_BRANCH_COUNT = 14 (0 BR, 14 BLR)
```
