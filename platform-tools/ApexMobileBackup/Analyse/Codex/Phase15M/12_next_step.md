# Next step

Do not patch or bypass the compatibility branch. A future phase may implement
an original, standalone graphics diagnostic with no Apex code or assets. Its
specification is:

1. create a normal EGL OpenGL ES context requesting ES 3.1;
2. record `GL_VERSION`, `GL_RENDERER`, and `GL_VENDOR`;
3. record presence of `GL_EXT_color_buffer_half_float` and
   `GL_EXT_color_buffer_float`;
4. create a floating-point color attachment using the extension-supported
   format route;
5. attach it to an FBO and record `glCheckFramebufferStatus` plus GL errors;
6. emit only local capability results and delete no client data.

The probe must not install, launch, instrument, patch, or depend on Apex. It
would validate an environment before any separately authorized client test.

```text
CLEANROOM_GRAPHICS_PROBE_SPEC_READY = YES
```
