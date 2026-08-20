# Screenshot classification

The images were inspected locally and are not published.

| Capture | Functional components |
| --- | --- |
| +5 s | Android immersive-mode tutorial, Android SystemUI ANR dialog with `Wait`, dark or blurred Apex application surface |
| +30 s | Same Android overlays, Lightspeed Studios logo beneath them, Apex-owned application dialog with only an `OK` action visible |
| +120 s | Same visible composition as +30 s |

The +30 s and +120 s images are byte-identical. No spinner, percentage, progress
bar, or changing progress indicator is visible. The screen is not black.

```text
SCREEN_5S_COMPONENTS = ANDROID_IMMERSIVE_TUTORIAL; SYSTEMUI_ANR_WAIT; DARK_APEX_SURFACE
SCREEN_30S_COMPONENTS = ANDROID_OVERLAYS; LIGHTSPEED_LOGO; APEX_APPLICATION_DIALOG_OK
SCREEN_120S_COMPONENTS = ANDROID_OVERLAYS; LIGHTSPEED_LOGO; APEX_APPLICATION_DIALOG_OK
WAIT_UI_VISUALLY_CHANGES = YES_BETWEEN_5S_AND_30S; NO_BETWEEN_30S_AND_120S
WAIT_UI_PROGRESS_VISIBLE = NO
WAIT_UI_TEXT = Wait
```

`Wait` is Android SystemUI text. It is not an Apex bootstrap status label.
