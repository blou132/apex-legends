# PotatoNV upload

The pinned executable rehashed to its authoritative Phase16D SHA256
immediately before launch. UI automation selected the sole bootrom target and
`Kirin 65x (A)`. `Disable FBLOCK` remained off; reboot remained at its normal
default. Start was invoked once.

The local-only log records successful verification, `hisi65x_a` xloader and
fastboot RAM uploads, the wait, connection, exact PRA-LX1/C33 information,
documented NV operation, reboot, and unlock-code output. Private values are
not reproduced here.

The log line `FBLOCK state: unlocked` describes the temporary RAM fastboot.
It does not prove that the stock bootloader was permanently unlocked.

```text
POTATONV_PROFILE_CONFIRMED = YES_KIRIN_65X_A
FBLOCK_CHANGE_REQUESTED = NO
POTATONV_EXECUTED = YES_SINGLE_BOUNDED_OPERATION
POTATONV_UPLOAD_COMPLETED = YES
```
