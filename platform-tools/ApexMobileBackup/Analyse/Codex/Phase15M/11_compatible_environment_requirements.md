# Compatible environment requirements

The minimum requirements proven by the branch are:

```text
REQUIRED_GLES_VERSION = OpenGL ES 3.1 (major == 3, minor >= 1)
REQUIRED_OTHER_GRAPHICS_FEATURES = bBuildForES31 true and r.Android.DisableOpenGLES31Support == 0
ES2_FALLBACK_AVAILABLE = NO
```

The separately reported float-render-target capability is true when:

```text
GL_EXT_color_buffer_half_float
OR
(OpenGL ES 3.x AND GL_EXT_color_buffer_float)
```

This extension predicate is a diagnostic requirement in this build, not an
operand of the confirmed rejecting branch. A future legitimate environment
should expose one of these routes so both the primary gate and reported
capability are satisfied.

No Vulkan feature, manifest OpenGL version, Android system property, renderer
name, or vendor name is a proven additional operand of this branch.
