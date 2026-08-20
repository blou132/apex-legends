# Auto writable runtime

`ApexGraphicsProbe` completed boot in normal writable mode with `-gpu auto`,
`-no-snapshot-load`, and `-no-snapshot-save`. `T0` was defined by
`sys.boot_completed=1`; logcat was cleared once immediately afterward.

SystemUI was clear at the first two targeted checks (`+1.250 s` and
`+15.440 s`). The first visible ANR checkpoint was `+30.265 s`. The correlated
ActivityManager event occurred at `+15.813 s` relative to T0, just after the
15-second check, and records a service that had already waited `20131 ms`.

Only the requested lightweight CPU/process snapshots were taken. No 30-second
baseline was taken after the ANR became visible. No UI interaction occurred.

```text
AUTO_WRITABLE_BOOTED = YES
AUTO_WRITABLE_SYSTEMUI_STABLE = NO
AUTO_WRITABLE_ANR_TIME = +30.265 s FIRST_VISIBLE_CHECKPOINT; +15.813 s EVENT
```
