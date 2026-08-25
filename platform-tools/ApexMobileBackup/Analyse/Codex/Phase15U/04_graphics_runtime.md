# Graphics and native runtime

The earlier emulator compatibility dialog did not appear. The physical client
created Mali EGL window surfaces successfully (`EGL_SUCCESS`), reported the
Mali-T830 OpenGL ES 3.2 renderer, executed code inside `libUE4.so`, loaded the
client launch level, and rendered the update interface.

One Mali diagnostic reported a disconnected producer and emitted a graphics
stack containing `libUE4.so`. It was not fatal: there was no fatal signal,
process crash, or Apex ANR; a subsequent EGL surface creation succeeded; the
process remained alive; and the update UI was rendered.

No runtime address, load bias, or private mapping is published or inferred.

```text
GLES_COMPAT_DIALOG_PRESENT = NO
PHYSICAL_DEVICE_GRAPHICS_GATE_PASSED = CONFIRMED
LIBUE4_RUNTIME_EVIDENCE = CONFIRMED
EGL_RUNTIME_EVIDENCE = CONFIRMED
OPENGL_RUNTIME_EVIDENCE = CONFIRMED
RHI_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
FATAL_GRAPHICS_OR_NATIVE_FAILURE = NO
MALI_DIAGNOSTIC_CLASS = NON_FATAL_TRANSIENT_SURFACE_DIAGNOSTIC
```
