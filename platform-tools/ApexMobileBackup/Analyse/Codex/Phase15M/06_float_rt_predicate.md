# Floating-point render-target predicate

`FUN_05db077c` obtains `GL_EXTENSIONS` and performs exact searches for:

- `GL_EXT_color_buffer_half_float` at Ghidra `0x26e343c`;
- `GL_EXT_color_buffer_float` at Ghidra `0x2803812`.

It also recognizes an OpenGL ES 3.x version from the `OpenGL ES 3.` prefix at
Ghidra `0x2683114`. The byte later read at global Ghidra `0xb9a9068` is set by
the following predicate:

```text
supports_float_rt =
    has(GL_EXT_color_buffer_half_float)
    || (is_gles3 && has(GL_EXT_color_buffer_float))
```
The value is displayed in the diagnostic and logged when absent. Crucially,
the assembly at Ghidra `0x59ef400-0x59ef414` does not test this byte. The
primary compatibility branch tests the CVar, build flag, minor, and major only.
Therefore float RT is a confirmed capability diagnostic in this build, not a
confirmed rejecting operand.

```text
FLOAT_RT_CHECK_SOURCE = glGetString(GL_EXTENSIONS) in FUN_05db077c
FLOAT_RT_REQUIRED_EXTENSION_OR_FEATURE = GL_EXT_color_buffer_half_float OR (GLES3 AND GL_EXT_color_buffer_float)
FLOAT_RT_PREDICATE = exact extension predicate above
FLOAT_RT_PART_OF_REJECTION_BRANCH = NO
```
