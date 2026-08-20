# Host renderer result

`host` booted and created the exact requested OpenGL ES 3.1 context. The
reported version passes `major == 3 && minor >= 1`. Both float extensions were
present, the `RGBA16F` attachment was created, and the FBO was complete with no
GL error. The booted AVD remained healthy for the required additional 30
seconds before clean shutdown.

```text
GPU_MODE = host
AVD_BOOTED = YES
PROBE_RAN = YES
GL_VENDOR = Google (ATI Technologies Inc.)
GL_RENDERER = Android Emulator OpenGL ES Translator (AMD Radeon(TM) RX Vega 10 Graphics)
GL_VERSION = OpenGL ES 3.1 (4.5.14761 Core Profile Context 21.30.23.04 30.0.13023.4001)
GLES31_GATE = PASS
HALF_FLOAT_EXTENSION = YES
FLOAT_EXTENSION = YES
FLOAT_RT_DIAGNOSTIC_PREDICATE = PASS
FLOAT_FBO_CREATED = YES
FLOAT_FBO_COMPLETE = YES
FBO_STATUS = 0x8CD5
GL_ERROR = GL_NO_ERROR (0x0000)
MODE_STABLE_30S = YES
```
