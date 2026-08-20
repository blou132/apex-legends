# GPU log correlation

Host renderer selection is confirmed in A and B by the Radeon-backed Android
Emulator OpenGL ES Translator reporting GLES 3.1. The auto control reports
SwiftShader GLES 3.0.

No emulator host log around any ANR reports context loss, device loss/reset,
swap failure, or EGL/OpenGL initialization failure. All cases, including auto,
show high SurfaceFlinger kernel CPU in ActivityManager diagnostics.

```text
HOST_RENDERER_ACTIVE = YES
ANR_RENDERER_RELATED_EVIDENCE = CORRELATED_SURFACEFLINGER_KERNEL_CPU_ONLY
HOST_GPU_ERROR_NEAR_ANR = NO
```

The evidence does not support host rendering as the sole cause.
