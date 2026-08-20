# Probe design

The original Java Activity creates an off-screen EGL pbuffer and requests an
exact OpenGL ES 3.1 context first. If that exact request fails, it requests a
generic ES 3.x context solely to measure the renderer's actual `GL_VERSION`.
The result passes only when the parsed values satisfy `major == 3 && minor >=
1`; ES 3.0 is explicitly insufficient.

After `eglMakeCurrent`, the probe records EGL vendor/version and
`GL_VENDOR`, `GL_RENDERER`, and `GL_VERSION`. GLES 3 extension discovery uses
`GL_NUM_EXTENSIONS` plus `glGetStringi`; the legacy string is only a fallback.
It tests these exact flags independently:

```text
GL_EXT_color_buffer_half_float
GL_EXT_color_buffer_float
```

The diagnostic predicate is:

```text
half_float || (GLES3 && float)
```

When permitted, the probe allocates a `GL_RGBA16F` renderbuffer, attaches it to
a framebuffer, calls `glCheckFramebufferStatus`, and records the GL error.
This verifies a real float attachment instead of trusting extension strings.

The result is written only to the application's private files directory and
read with `run-as`. No network API or permission is present.
