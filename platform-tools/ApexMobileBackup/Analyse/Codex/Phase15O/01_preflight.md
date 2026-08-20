# Preflight

`adb devices -l` returned no endpoint before the phase. WHPX acceleration
returned exit code `0` and reported `WHPX(10.0.26200) is installed and usable`.

`ApexPhase9Lab` was cold-booted with:

```text
-gpu host
-read-only
-no-snapshot-load
-no-snapshot-save
```

No wipe or persistent AVD edit was performed. Android reached
`sys.boot_completed=1`. After the required 60-second wait, the targeted window
dump showed one visible SystemUI ANR window, so the preflight failed before
package, OBB, cache, or network checks.

```text
PHYSICAL_DEVICE_PRESENT = NO
WHPX_ACCELERATION = CONFIRMED
AVD_BOOT_COMPLETED = YES
GPU_MODE = host
SYSTEMUI_PREFLIGHT_STABLE = NO
PACKAGE_STATE_VALID = NOT_REACHED
OBB_STATE_VALID = NOT_REACHED
VALIDATION_CACHE_STATE = NOT_REACHED
```
