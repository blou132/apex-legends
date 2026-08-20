# Phase15N - Clean-room GLES31 graphics environment probe

Date: 2026-08-20

## Executive result

No physical Android device was present. WHPX was usable, the Android 36.1
Google Play x86_64 image was already installed, and the preserved
`ApexPhase9Lab` AVD was neither booted nor modified.

A separate `ApexGraphicsProbe` AVD was created without a download. An original
`local.graphicsprobe` application was built entirely from committed source and
local SDK/JDK tools. It requests no Internet permission and contains no Apex
identifier or material. It directly creates EGL contexts, parses
`GL_VERSION`, checks the two exact float extensions, and verifies a real
`RGBA16F` framebuffer attachment.

All six renderer modes explicitly listed by emulator `36.4.9.0` were tested
one at a time with cold boots and snapshots disabled. `auto`, `software`,
`lavapipe`, `swiftshader`, and `swangle` all resolved to Google SwiftShader
OpenGL ES 3.0. They fail the exact Phase15M GLES 3.1 predicate even though the
float extension predicate and real FBO test pass.

`host` created the exact OpenGL ES 3.1 context, exposed both float extensions,
completed the FBO with `GL_NO_ERROR`, and remained stable for 30 seconds. It is
the sole legitimate candidate and satisfies the resolved Phase15M environment
gate. Apex was not installed or launched, so no client behavior is inferred.

All emulators were stopped. ADB is empty, no physical device appeared, and the
three preserved `ApexPhase9Lab` file hashes and timestamps match preflight.
Raw APK, signing material, run harness, and emulator logs remain local-only.

## Required result

```text
WHPX_ACCELERATION = CONFIRMED WHPX(10.0.26200) INSTALLED_AND_USABLE; EXIT_CODE_0
SYSTEM_IMAGE_LOCAL = YES system-images;android-36.1;google_apis_playstore;x86_64

PROBE_AVD_CREATED = YES ApexGraphicsProbe
PROBE_BUILD_RESULT = SUCCESS LOCAL_ONLY_APK_NO_INTERNET_PERMISSION

SUPPORTED_GPU_MODES = auto, host, software, lavapipe, swiftshader, swangle

AUTO_GL_VENDOR = Google (Google Inc.)
AUTO_GL_RENDERER = Android Emulator OpenGL ES Translator (Google SwiftShader)
AUTO_GL_VERSION = OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
AUTO_GLES31_GATE = FAIL
AUTO_FLOAT_RT = PASS
AUTO_FBO_COMPLETE = YES

HOST_GL_VENDOR = Google (ATI Technologies Inc.)
HOST_GL_RENDERER = Android Emulator OpenGL ES Translator (AMD Radeon(TM) RX Vega 10 Graphics)
HOST_GL_VERSION = OpenGL ES 3.1 (4.5.14761 Core Profile Context 21.30.23.04 30.0.13023.4001)
HOST_GLES31_GATE = PASS
HOST_FLOAT_RT = PASS
HOST_FBO_COMPLETE = YES

SOFTWARE_GL_VENDOR = Google (Google Inc.)
SOFTWARE_GL_RENDERER = Android Emulator OpenGL ES Translator (Google SwiftShader)
SOFTWARE_GL_VERSION = OpenGL ES 3.0 (OpenGL ES 3.0 SwiftShader 4.0.0.1)
SOFTWARE_GLES31_GATE = FAIL
SOFTWARE_FLOAT_RT = PASS
SOFTWARE_FBO_COMPLETE = YES

PASSING_GLES31_MODES = host
LEGITIMATE_GRAPHICS_MODE_CANDIDATE = host

APEX_PHASE15M_GATE_COMPATIBLE = YES
APEX_FLOAT_RT_DIAGNOSTIC = PASS

APEX_PHASE9LAB_UNCHANGED = YES

FINAL_GATE = A LEGITIMATE_GLES31_AND_FLOAT_RT_MODE_FOUND
COMMIT = Probe legitimate GLES31 emulator modes Phase15N
GIT_STATUS = CLEAN_EXPECTED_AFTER_COMMIT
```

## Evidence boundary

Phase15N proves only the exposed capabilities of the clean-room probe under
the installed emulator modes. It does not prove Apex startup, native graphics
success, splash dismissal, Lua/Login reachability, server access, or gameplay.
