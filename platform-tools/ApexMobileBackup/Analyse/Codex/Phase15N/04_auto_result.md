# Auto renderer result

`auto` booted and ran the probe successfully. It selected Google SwiftShader,
exposed only OpenGL ES 3.0, and therefore failed the exact Phase15M GLES 3.1
predicate. Both float extensions were exposed and the real `RGBA16F` FBO was
complete without a GL error.

```text
GPU_MODE = auto
AVD_BOOTED = YES
PROBE_RAN = YES
GL_VENDOR = Google (Google Inc.)
GL_RENDERER = Android Emulator OpenGL ES Translator (Google SwiftShader)
GL_VERSION = OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
GLES31_GATE = FAIL
HALF_FLOAT_EXTENSION = YES
FLOAT_EXTENSION = YES
FLOAT_RT_DIAGNOSTIC_PREDICATE = PASS
FLOAT_FBO_CREATED = YES
FLOAT_FBO_COMPLETE = YES
FBO_STATUS = 0x8CD5
GL_ERROR = GL_NO_ERROR (0x0000)
MODE_STABLE_30S = NOT_REQUIRED_GATE_FAILED
```
