# Auto plus read-only control

The disposable AVD booted with `-gpu auto -read-only` and snapshot load/save
disabled. SurfaceFlinger reported Google SwiftShader and OpenGL ES 3.0, proving
that this control did not use the Radeon host renderer.

```text
ANR_0S = NO (actual 0.000 s)
ANR_15S = NO (actual 15.070 s)
ANR_30S = NO (actual 30.112 s)
ANR_60S = YES (actual 60.173 s)
ANR_90S = NOT_REACHED_ANR_STOP
ANR_120S = NOT_REACHED_ANR_STOP
AUTO_READONLY_SYSTEMUI_STABLE = NO
AUTO_READONLY_ANR_TIME = 60.173 s
```

ActivityManager records `SystemUIService` execution exceeding `20515 ms`.
This control invalidates the host renderer as the sole failure condition.
