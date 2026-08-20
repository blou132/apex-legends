# BCD audit

The Phase15B rule permits elevated, read-only BCD inspection only when WHPX is
enabled but `emulator.exe -accel-check` still fails.

Acceleration succeeded with exit code `0`, and Windows reports the hypervisor
present. Therefore `bcdedit` was not executed and no elevation was requested.

```text
HYPERVISOR_LAUNCH_TYPE = NOT_AUDITED_ACCELERATION_CONFIRMED
BCD_CHANGE = NOT_PERFORMED
```
