# Runtime storage

The prelaunch `/data` reading was above the 2 GiB operational target and was
classified `READY`. The runtime never approached either hard guard.

| Checkpoint | Free `/data` KiB | Change from launch KiB |
| --- | ---: | ---: |
| Prelaunch | `2122084` | n/a |
| Launch baseline | `2164200` | `0` |
| +5 s | `2164548` | `+348` |
| +15 s | `2164544` | `+344` |
| +30 s | `2164540` | `+340` |
| +60 s | `2164540` | `+340` |
| +120 s | `2164472` | `+272` |
| +180 s | `2148268` | `-15932` |
| Post-stop | `2148248` | `-15952` |

Positive values mean more free space than at launch. The final runtime delta is
`-15952 KiB` of free space, equivalent to approximately `15.58 MiB` consumed.
The variation between preflight and launch is system free-space variation, not
an Apex cleanup action; no file was deleted during this phase.

```text
FREE_SPACE_PRELAUNCH = 2122084 KiB
FREE_SPACE_AT_LAUNCH = 2164200 KiB
FREE_SPACE_POSTRUN = 2148248 KiB
RUNTIME_STORAGE_DELTA = -15952 KiB free (15.58 MiB consumed)
STORAGE_RUNTIME_GUARD_TRIGGERED = NO
```
