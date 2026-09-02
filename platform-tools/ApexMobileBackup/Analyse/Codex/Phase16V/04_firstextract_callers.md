# First-extract callers

One exact direct caller reaches the first-extract-success owner continuation:

| Source ELF | Target ELF | Caller | Exact identifier |
|---|---|---|---|
| `0x07ff8834` | `0x07ff6f04` | `FUN_080f87d0` | `DolphinCallback::OnDolphinFirstExtractSuccess` |

The callback loads `X0` from `[callback + 0x08]`, null-tests it, and then calls
the owner continuation. This reproduces the pre-existing control edge.

```text
FIRSTEXTRACT_OWNER_DIRECT_BRANCH_COUNT = 1
EXACT_BRANCH_SCANNER_CONTROL_VALID = YES
```
