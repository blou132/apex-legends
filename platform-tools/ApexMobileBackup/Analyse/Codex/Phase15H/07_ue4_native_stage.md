# Native and UE4 post-resume stage

The new post-resume evidence is process-scoped graphics startup. At about
`+16.305 s`, the Apex process entered EGL initialization and reported
`EGL_BAD_ATTRIBUTE` probes. At `+17.127 s`, the same process searched its ARM64
library locations for Vulkan layers. Mesa render-node errors followed, but the
application stayed alive and rendered the Lightspeed splash and wait UI by the
`+30 s` screenshot.

This confirms native graphics activity and a rendered client splash after the
validated-OBB resume boundary. It does not identify a precise UE4 function,
GameEngine/GameInstance transition, world creation, gameplay tick, render
thread name, or Vulkan success. The exact `nativeResumeMainInit` name does not
appear after post-resume T0; Phase10 remains the prior direct method witness.

```text
NATIVE_RESUME_RUNTIME_WITNESS = CONFIRMED
NATIVE_RESUME_RUNTIME_WITNESS_DETAIL = PROCESS_SCOPED_NATIVE_GRAPHICS_ACTIVITY
NATIVE_RESUME_MAIN_INIT_NAME_AFTER_T0 = NO_NEW_EVIDENCE
UE4_POST_RESUME_STAGE = GRAPHICS_BACKEND_INITIALIZATION_AND_RENDERED_SPLASH
EXACT_UE4_FUNCTION = UNKNOWN
GAMEPLAY_FRAME = NOT_CONFIRMED
```
