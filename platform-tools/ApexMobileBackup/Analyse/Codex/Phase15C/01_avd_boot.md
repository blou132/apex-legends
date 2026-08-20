# AVD boot

The physical-device exclusion check runs before acceleration and AVD checks.
It found one physical endpoint, so the run stopped immediately.

```text
PHYSICAL_DEVICE_PRESENT = CONFIRMED YES
EMULATOR_ENDPOINT_PRESENT_AT_PREFLIGHT = NO
WHPX_ACCELERATION = NOT_RECHECKED_PHYSICAL_DEVICE_STOP
AVD_CONFIGURATION_RECHECK = NOT_ATTEMPTED_STOP_RULE
AVD_STARTED = NO
```

Phase15B remains authoritative for the last successful WHPX acceleration and
static AVD-configuration results. Phase15C does not supersede those values.
