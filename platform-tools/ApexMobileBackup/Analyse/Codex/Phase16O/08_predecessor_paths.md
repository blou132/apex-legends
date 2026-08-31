# Predecessor paths

There are zero semantic paths from the function entry to `CreateDolphin`.
For completeness, the impossible post-throw fallthrough has six raw variants:

| Temporary release | Debug log | Merge |
|---|---|---|
| `x23 == 0`, no release | threshold below 5 | `0x05a314b8` |
| `x23 == 0`, no release | threshold at least 5 | `0x05a314b8` |
| `x23 != 0`, manager ready, release by `BLR` | threshold below 5 | `0x05a314b8` |
| `x23 != 0`, manager ready, release by `BLR` | threshold at least 5 | `0x05a314b8` |
| `x23 != 0`, manager initialized, then release | threshold below 5 | `0x05a314b8` |
| `x23 != 0`, manager initialized, then release | threshold at least 5 | `0x05a314b8` |

All six require `std::__throw_length_error` at `0x05a3144c` to return. They
are raw disassembly variants, not legitimate reaching paths.

```text
CREATEDOLPHIN_REACHING_PATH_COUNT = 0_SEMANTIC (6_RAW_POST_NORETURN_VARIANTS)
```
