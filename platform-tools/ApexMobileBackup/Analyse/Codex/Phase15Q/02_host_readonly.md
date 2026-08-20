# Host plus read-only

The disposable AVD booted with `-gpu host -read-only`, snapshot load/save
disabled. SurfaceFlinger reported the Radeon-backed emulator translator and
OpenGL ES 3.1, confirming that host rendering was active.

```text
ANR_0S = NO (actual 0.012 s)
ANR_15S = NO (actual 15.124 s)
ANR_30S = YES (actual 30.043 s)
ANR_60S = NOT_REACHED_ANR_STOP
ANR_90S = NOT_REACHED_ANR_STOP
ANR_120S = NOT_REACHED_ANR_STOP
HOST_READONLY_SYSTEMUI_STABLE = NO
HOST_READONLY_ANR_TIME = 30.043 s
```

ActivityManager records `KeyguardService` execution exceeding `20053 ms`.
