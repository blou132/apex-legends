# SIGTRAP and registers

No `SIGTRAP` was received during the only active window. Therefore no
function-entry register state is claimed.

```text
CVERSIONMGR_INIT_ENTRY_TRAP = NO
PC_CAPTURED = NO
SP_CAPTURED = NO
LR_CAPTURED = NO
X0_X7_CAPTURED = NO
```

The static ABI proof remains valid, but it cannot substitute for runtime
register capture.
