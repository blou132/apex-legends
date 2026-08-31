# Unwind boundaries

Binary search of `.eh_frame_hdr` and bounded FDE decoding independently
confirm these ELF ranges:

| Role | FDE range |
|---|---|
| Direct call containing function | `0x05a311a8...0x05a31ca8` |
| Immediate upper caller | `0x07ff6f04...0x07ff760c` |
| Post-extraction callback | `0x07ff87d0...0x07ff883c` |

The target FDE uses CIE augmentation `zR`, with no LSDA pointer. The direct
call function starts and ends exactly where Ghidra places `FUN_05b311a8` after
applying the validated bias.

```text
GHIDRA_FUNCTION_BOUNDARY_CORRECT = YES
UNWIND_FUNCTION_START = 0x05a311a8
UNWIND_FUNCTION_END = 0x05a31ca8
```
