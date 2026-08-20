# Graphics gate logic

The native branch at Ghidra `0x59ef3f4-0x59ef414` is reconstructed as:

```text
supports_es31 = (gles_major == 3) && (gles_minor >= 1)

primary_es31_ok =
    (r.Android.DisableOpenGLES31Support == 0)
    && bBuildForES31
    && supports_es31

if primary_es31_ok:
    selected_graphics_level = (gles_minor > 1) ? 3 : 2
    initialize_selected_opengl_context(gles_major, gles_minor)
else:
    selected_graphics_level = 1
    if !bBuildForES2:
        build_diagnostics(
            supports_es31,
            supports_float_rt
        )
        invoke_messagebox(message, title)
    # If bBuildForES2 is true, continue on the ES2 fallback without a dialog.

make_context_current()
return true
```

The exact dialog condition for the observed ES31-packaged route is:

```text
show_dialog = !primary_es31_ok && !bBuildForES2
```

`supports_float_rt` is passed into diagnostic construction but is not an
operand of `primary_es31_ok` or `show_dialog`. This corrects the prior
English-text inference that float RT itself necessarily selected the failure
branch.
