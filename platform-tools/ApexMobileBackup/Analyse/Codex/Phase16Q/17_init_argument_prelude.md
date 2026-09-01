# Init argument prelude

The exact client-side setup before ELF `0x05a2f0ac` is:

| Register | Source | Recoverability |
|---|---|---|
| `X0` | `[DolphinUpdater+0x1f0]` | exact |
| `X1` | `SP+0x680` | address exact; construction before frontier |
| `X2` | `SP+0x280` | address exact; construction before frontier |
| `X3` | `X21` | carried from opaque prefix |
| `X4` | null | exact |
| `X5` | `[DolphinUpdater+0x1f8]` | exact |

No write to either stack local occurs in the readable path before Init. Later
uses cannot retroactively define the values at the dispatch.

The sources are structurally compatible with the callee-side Init ABI from
Phase16J, but opaque values are not assigned semantic names.

```text
INIT_X1_LOCAL_ROLE = STACK_LOCAL_CONSTRUCTED_BEFORE_READABLE_FRONTIER_ROLE_UNKNOWN
INIT_X2_LOCAL_ROLE = STACK_LOCAL_CONSTRUCTED_BEFORE_READABLE_FRONTIER_ROLE_UNKNOWN
INIT_X3_X21_SOURCE = OPAQUE_PREFIX_UNKNOWN
INIT_ARGUMENT_SEMANTIC_MATCH = PARTIAL_ABI_COMPATIBLE_SOURCES_EXACT_SEMANTICS_OPAQUE
```
