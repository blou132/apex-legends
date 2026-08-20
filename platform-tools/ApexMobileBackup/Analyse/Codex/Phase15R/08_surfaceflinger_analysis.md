# SurfaceFlinger analysis

SurfaceFlinger is a confirmed major CPU consumer in the ANR window:

- `94%` process CPU, comprising `1.1%` user and `93%` kernel;
- a focused `3.820 s` interval shows `93%` for SurfaceFlinger;
- its `RenderEngine` thread accounts for `78%`, including `77%` kernel CPU.

The delayed bugreport stack later finds RenderEngine waiting normally on its
condition variable, so it does not preserve the exact hot native frame. The CPU
accounting nevertheless explicitly identifies RenderEngine as the hot thread
during the ANR interval.

The SystemUI trace contains no call or wait edge to SurfaceFlinger,
RenderEngine, WindowManager, or DisplayManager. SurfaceFlinger is therefore a
confirmed CPU-pressure contributor, not a proved synchronous dependency of the
SystemUI service timeout.

```text
SURFACEFLINGER_HIGH_CPU_CONFIRMED = YES
SURFACEFLINGER_BUSY_THREAD = RenderEngine
SYSTEMUI_DIRECTLY_WAITS_ON_SURFACEFLINGER = NO
```
