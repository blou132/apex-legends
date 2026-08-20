# Phase15M - Native GLES31 / float render-target gate

Date: 2026-08-20

## Executive result

The exact Phase15L title reaches `FUN_059ef114` through a direct AArch64
`ADRP+ADD` at Ghidra `0x59ef9b0`. That function is the confirmed
`AndroidOpenGL.cpp` compatibility gate and message builder. It parses
`glGetString(GL_VERSION)`, reads `bBuildForES31`, reads the
`r.Android.DisableOpenGLES31Support` CVar, and selects the ES3.1 path only when
the CVar is zero, the package flag is true, and the parsed version is major 3
with minor at least 1.

When that primary condition fails, graphics level 1 is selected. The dialog is
shown only when `AndroidRuntimeSettings.bBuildForES2` is also false. Phase15L's
runtime result therefore matches the exact native route with no packaged ES2
fallback.

The float-render-target byte is independently initialized from
`GL_EXT_color_buffer_half_float` or, on GLES3, `GL_EXT_color_buffer_float`.
It is included in diagnostic text but is not tested by the success/failure
branch. This corrects the earlier interpretation based only on dialog prose.

The success path calls `FUN_059efff8`, which performs EGL/OpenGL backend
initialization, then reaches `FUN_059efe00` for `eglMakeCurrent`. The gate runs
after preliminary EGL setup and before final selected context setup, so its
position is `AFTER_PARTIAL_RHI_INIT`.

The direct dialog target is Ghidra `0x81dea3c` / ELF `0x80dea3c`, called with
`(0, message, title)`. Its static body is protected/non-disassemblable. A
separate native cache resolves constructor/string methods and `show()I`, making
a reflection-based MessageBox bridge probable, but the synchronous native edge
to `MessageBox01.show()` is not proven. Native waiting remains unknown and
bootstrap blocking remains probable.

## Exact branch

```text
supports_es31 = (major == 3) && (minor >= 1)
primary_es31_ok =
    (r.Android.DisableOpenGLES31Support == 0)
    && bBuildForES31
    && supports_es31

show_dialog = !primary_es31_ok && !bBuildForES2
```

`supports_float_rt` is diagnostic-only for this branch.

## Required result

```text
NATIVE_MESSAGEBOX_FUNCTION = FUN_081dea3c; GHIDRA 0x81dea3c; ELF 0x80dea3c; PROBABLE SEMANTICS
GRAPHICS_ERROR_BUILDER_FUNCTION = FUN_059ef114; GHIDRA 0x59ef114; ELF 0x58ef114

GLES31_CHECK_SOURCE = glGetString(GL_VERSION) -> parsed major/minor
GLES31_REQUIRED_VALUE = OpenGL ES 3.1
GLES31_PREDICATE = major == 3 && minor >= 1

FLOAT_RT_CHECK_SOURCE = glGetString(GL_EXTENSIONS) in FUN_05db077c
FLOAT_RT_REQUIRED_EXTENSION_OR_FEATURE = GL_EXT_color_buffer_half_float OR (GLES3 AND GL_EXT_color_buffer_float)
FLOAT_RT_PREDICATE = exact extension predicate; diagnostic-only in gate branch

ES2_FALLBACK_CHECK_SOURCE = AndroidRuntimeSettings.bBuildForES2
ES2_FALLBACK_AVAILABLE = CONFIRMED NO

GRAPHICS_GATE_LOGIC = show_dialog iff !(CVar==0 && bBuildForES31 && major==3 && minor>=1) && !bBuildForES2
GRAPHICS_GATE_FAILURE_BEHAVIOR = invoke protected dialog target, run JNI helper, common graphics tail, return 1
NATIVE_WAITS_FOR_DIALOG_RESULT = UNKNOWN

GRAPHICS_GATE_SUCCESS_NEXT_FUNCTION = FUN_059efff8 (GHIDRA 0x59efff8; ELF 0x58efff8)
GRAPHICS_GATE_SUCCESS_STAGE = OPENGL_EGL_BACKEND_INITIALIZATION; AFTER_PARTIAL_RHI_INIT

GRAPHICS_SUCCESS_CONTROLS_SPLASH_DISMISS = NO_EVIDENCE
NATIVE_SPLASH_DISMISS_TRIGGER = UNKNOWN

REQUIRED_GLES_VERSION = OpenGL ES 3.1
REQUIRED_FLOAT_RT_FEATURE = extension predicate above for reported capability; not a rejecting operand
REQUIRED_OTHER_GRAPHICS_FEATURES = bBuildForES31 true; DisableOpenGLES31Support CVar zero

CLEANROOM_GRAPHICS_PROBE_SPEC_READY = YES

FINAL_GATE = A EXACT_GRAPHICS_GATE_AND_SUCCESS_PATH_RESOLVED
COMMIT = Resolve native graphics compatibility gate Phase15M
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Limits

- No runtime address or load bias is inferred.
- The protected direct MessageBox target prevents proving native wait behavior.
- No native splash-dismiss trigger is present in the resolved neighborhood.
- Raw disassembly, scripts, binaries, DEX, APK, OBB, and PAK content remain
  local-only and uncommitted.
