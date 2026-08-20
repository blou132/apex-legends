# Graphics error builder

`FUN_059ef114` at Ghidra `0x59ef114` / ELF `0x58ef114` is the confirmed
graphics compatibility function and error builder. Evidence includes:

- exact title materialization at Ghidra `0x59ef9b0`;
- direct reads of `bBuildForES31` and `bBuildForES2` from
  `/Script/AndroidRuntimeSettings.AndroidRuntimeSettings`;
- direct use of `r.Android.DisableOpenGLES31Support`;
- parsed OpenGL major/minor values;
- direct read of the float-render-target capability byte;
- conditional construction of the GLES31, float RT, and ES2 diagnostic lines;
- direct call to `0x81dea3c` with message and caption;
- source path evidence for `AndroidOpenGL.cpp`.

The builder's diagnostic formatting includes the independently calculated
GLES31 and float RT booleans. The float value is not part of the branch that
selects ES31 success versus fallback.

```text
GRAPHICS_ERROR_BUILDER_FUNCTION = FUN_059ef114
GRAPHICS_ERROR_BUILDER_GHIDRA = 0x59ef114
GRAPHICS_ERROR_BUILDER_ELF = 0x58ef114
```
