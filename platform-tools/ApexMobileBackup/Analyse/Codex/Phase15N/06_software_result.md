# Software renderer results

The canonical `software` mode and the three other explicit software-oriented
modes all booted and ran successfully. Every one resolved GLES to Google
SwiftShader, exposed OpenGL ES 3.0, failed the exact GLES 3.1 gate, and passed
the independent extension plus real-FBO diagnostics.

| Mode | GLES | GLES31 | Float predicate | FBO complete |
|---|---|---|---|---|
| `software` | 3.0 | FAIL | PASS | YES |
| `lavapipe` | 3.0 | FAIL | PASS | YES |
| `swiftshader` | 3.0 | FAIL | PASS | YES |
| `swangle` | 3.0 | FAIL | PASS | YES |

Canonical software result:

```text
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
