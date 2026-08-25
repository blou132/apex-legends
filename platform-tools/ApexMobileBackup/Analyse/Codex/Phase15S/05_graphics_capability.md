# Graphics capability

Read-only SurfaceFlinger state exposes the device's active graphics stack:

```text
EGL_IMPLEMENTATION = 1.4 Midgard-r20p0-01rel0
GL_VENDOR = ARM
GL_RENDERER = Mali-T830
GL_VERSION = OpenGL ES 3.2 v1.r20p0-01rel0.9a7fca3f7dd712a473937294a8ae24b1
RO_OPENGLES_VERSION = 196610
GL_EXT_color_buffer_half_float = PRESENT
GL_EXT_color_buffer_float = PRESENT
```

The platform therefore exceeds the OpenGL ES 3.1 version requirement and
exposes both extensions used by the Phase15M floating-point render-target
predicate. No probe installation, graphics spoofing, or Apex launch was needed.

```text
GLES31_AVAILABLE = YES
FLOAT_RT_CAPABILITY = YES_BOTH_REQUIRED_EXTENSION_PATHS_PRESENT
```
