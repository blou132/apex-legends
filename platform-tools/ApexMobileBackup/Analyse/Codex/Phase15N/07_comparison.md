# Renderer comparison

| Mode | Actual renderer | GL version | GLES31 | Float predicate | FBO | Stable 30s |
|---|---|---|---|---|---|---|
| `auto` | Google SwiftShader | ES 3.0 | FAIL | PASS | COMPLETE | not required |
| `host` | AMD Radeon host translation | ES 3.1 | PASS | PASS | COMPLETE | YES |
| `software` | Google SwiftShader | ES 3.0 | FAIL | PASS | COMPLETE | not required |
| `lavapipe` | Google SwiftShader | ES 3.0 | FAIL | PASS | COMPLETE | not required |
| `swiftshader` | Google SwiftShader | ES 3.0 | FAIL | PASS | COMPLETE | not required |
| `swangle` | Google SwiftShader | ES 3.0 | FAIL | PASS | COMPLETE | not required |

All modes were cold-booted with snapshot load/save disabled and tested one at a
time with the same final APK. The only passing GLES 3.1 mode is `host`; there
is no multiple-mode ambiguity.
