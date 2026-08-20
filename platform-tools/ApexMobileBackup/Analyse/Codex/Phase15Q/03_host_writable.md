# Host plus writable

The same disposable AVD booted normally writable with `-gpu host` and snapshot
load/save disabled. SurfaceFlinger again reported the Radeon-backed emulator
translator and OpenGL ES 3.1.

```text
ANR_0S = NO (actual 0.000 s)
ANR_15S = NO (actual 15.195 s)
ANR_30S = YES (actual 30.114 s)
ANR_60S = NOT_REACHED_ANR_STOP
ANR_90S = NOT_REACHED_ANR_STOP
ANR_120S = NOT_REACHED_ANR_STOP
HOST_WRITABLE_SYSTEMUI_STABLE = NO
HOST_WRITABLE_ANR_TIME = 30.114 s
```

ActivityManager records `KeyguardService` execution exceeding `20259 ms`.
Because both host cases fail at the same checkpoint, a read-only interaction is
not supported.
