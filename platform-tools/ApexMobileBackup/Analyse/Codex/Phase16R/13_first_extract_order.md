# First-extract order

The direct listing corrects the earlier gate description for `owner+0x45`:

- zero selects `x4 = NULL` before the shared Init dispatch;
- nonzero enters the `CheckUpdate, first extract pak` branch, prepares an
  alternate first-extract argument, and rejoins the same dispatch;
- first-extract success later invokes the callback continuation that clears
  `owner+0x45`.

Thus `+0x45` is a persistent first-extract Init-mode selector, not an absolute
requirement that must be zero for Init. In the first-extract workflow, the
success callback and clear occur after the Init that started that workflow.
No retained run contains the callback marker.

```text
FIRST_EXTRACT_RELATIVE_TO_INIT = AFTER
OWNER_PLUS_0X45_MODEL_CORRECTION = INIT_ARGUMENT_MODE_SELECTOR_NOT_DISPATCH_BLOCKER
```
