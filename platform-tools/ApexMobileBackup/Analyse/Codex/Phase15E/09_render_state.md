# Render and display state

## Confirmed observations

- At `+3.430 s`, an Apex render thread opens GFXSTREAM GLES1/GLES2 emulation
  libraries. This is Android/emulator graphics initialization and is not by
  itself proof of the UE4 renderer.
- After `libUE4.so` loads, a UE4-tagged splash starts at `+10.603 s`.
- Android creates/manages a `GameActivity` window, reports its insets, and marks
  the activity displayed at `+13.758 s`.
- Android later marks `DownloaderActivity` displayed at `+15.864 s`, after
  which the `GameActivity` window becomes hidden.

Android's `Displayed` event is evidence that the activity window reached its
first drawn/displayed boundary. It does not reveal the pixels, prove a rendered
UE4 gameplay frame, or identify why the user-visible content may remain black.

## Limits

- No exact `surfaceCreated` callback is logged.
- No unambiguous UE4 Vulkan/OpenGL renderer initialization is logged.
- Surface permission checks and a downloader surface-sync timeout are not a
  proven cause; the downloader activity is subsequently reported displayed.
- No screenshot or frame buffer was captured.

```text
RENDER_SURFACE_CREATED = CONFIRMED ANDROID_ACTIVITY_WINDOW
FIRST_FRAME_EVIDENCE = CONFIRMED ANDROID_ACTIVITY_DISPLAYED_BOUNDARY
WINDOW_VISIBLE = CONFIRMED YES
UE4_GAMEPLAY_FRAME = UNKNOWN
BLACK_SCREEN_CAUSE = UNKNOWN
```
