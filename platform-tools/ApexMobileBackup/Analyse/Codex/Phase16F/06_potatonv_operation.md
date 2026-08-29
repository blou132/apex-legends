# PotatoNV operation

Immediately before execution, the pinned PotatoNV executable rehashed to:

`2C1964BF2E4F8774E1688D01D140EF2BAD3CAF1077E17C6F7C27400F3C7707A7`

The selected target was the single Huawei bootrom endpoint. The selected and
upstream-documented PRA profile was `Kirin 65x (A)`. `Disable FBLOCK` remained
unchecked and normal reboot remained enabled.

PotatoNV verified and uploaded the `hisi65x_a` RAM images. It then waited for
the temporary fastboot endpoint and ended with `Timeout error`. Windows
correlated that interval with an unbound `Fastboot2.0` endpoint. The operation
did not reach device information reading, NV writes, code generation, or
reboot.

The raw PotatoNV log remains local-only.

```text
POTATONV_PROFILE_CONFIRMED = KIRIN_65X_A
FBLOCK_CHANGE_REQUESTED = NO
POTATONV_EXECUTED = YES_BOUNDED_ATTEMPT
POTATONV_COMPLETED = NO
POTATONV_FAILURE = TEMP_FASTBOOT_DRIVER_MISSING_DURING_ATTEMPT
```
