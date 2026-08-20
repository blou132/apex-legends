# Preflight

`adb devices -l` returned no endpoint before the run. WHPX acceleration exited
with code `0` and reported `WHPX(10.0.26200) is installed and usable`.
`ApexPhase9Lab` was the only AVD booted, using:

```text
-gpu host
-read-only
-no-snapshot-load
-no-snapshot-save
```

No persistent AVD setting was edited and `-wipe-data` was not used. Because the
SystemUI gate failed at the `30 s` checkpoint, package, OBB, validation-cache,
and network preflight stages were not reached.

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES
GPU_MODE = host
PACKAGE_STATE_VALID = NOT_REACHED
OBB_STATE_VALID = NOT_REACHED
VALIDATION_CACHE_REUSED = NOT_REACHED
```
