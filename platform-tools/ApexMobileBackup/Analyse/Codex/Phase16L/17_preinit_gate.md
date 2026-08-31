# Pre-Init gate

There is no proven client Init dispatch, so no predecessor condition can be
classified as selecting `GCloudDolphinImp::Init`. The callback null check in
the upper post-extraction path does not select the missing bootstrap owner.

```text
DOLPHIN_INIT_SELECTION_GATE = UNKNOWN
DOLPHIN_ALTERNATIVE_BRANCH = UNKNOWN
```
