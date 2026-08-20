# Success and failure paths

## Success

The direct success path is:

1. `FUN_059efe80` obtains/creates the Android OpenGL state object;
2. `FUN_059efff8` (Ghidra `0x59efff8`, ELF `0x58efff8`) performs selected
   EGL/OpenGL initialization through `eglGetDisplay`, `eglInitialize`,
   `eglBindAPI`, `eglQuerySurface`, and `eglQueryString`;
3. the common tail calls `FUN_059efe00` (ELF `0x58efe00`), which uses
   `eglMakeCurrent`, `glFlush`, and `eglGetCurrentContext`;
4. `FUN_059ef114` returns `1`.

The capability initializer can create a preliminary EGL context before the
version/extension query. The compatibility decision therefore occurs
`AFTER_PARTIAL_RHI_INIT`, before final selected OpenGL context initialization.

```text
GRAPHICS_GATE_SUCCESS_NEXT_FUNCTION = FUN_059efff8
GRAPHICS_GATE_SUCCESS_STAGE = OPENGL_EGL_BACKEND_INITIALIZATION
```
## Failure without ES2 fallback

The failure path builds the diagnostic, calls direct target Ghidra
`0x81dea3c` with `(0, message, title)`, then calls `FUN_062f11e8`. The latter
obtains/attaches a JNI environment and can invoke one Java-VM-side virtual
method, but no exit or splash operation is statically identifiable.

After cleanup, the common tail still calls `FUN_059efe00` and returns `1`.
There is no direct `return false`, process termination, or proven exit request.

DEX proves that `MessageBox01.show()` waits for a button. The protected body at
`0x81dea3c` prevents proving that this native call synchronously invokes that
specific `show()I` slot, so:

```text
GRAPHICS_GATE_FAILURE_BEHAVIOR = BUILD_AND_INVOKE_DIALOG_THEN_COMMON_GRAPHICS_TAIL_RETURN_1
NATIVE_WAITS_FOR_DIALOG_RESULT = UNKNOWN
DIALOG_BLOCKS_BOOTSTRAP = PROBABLE
```
