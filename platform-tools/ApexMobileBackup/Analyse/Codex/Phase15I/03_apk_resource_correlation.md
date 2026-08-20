# APK resource correlation

## Confirmed splash resources

| Resource | Type | Referenced by |
| --- | --- | --- |
| `video_splash` | layout and view identifier | `GameActivity.onCreate` |
| `splash_video` | raw MP4 resource | `GameActivity.onCreate` fallback URI |
| `AspectVideoView` | Android view class | `video_splash` layout |

The binary layout is a centered `RelativeLayout` containing
`com.epicgames.ue4.AspectVideoView`. `GameActivity.onCreate` inflates it into
the splash dialog and starts playback through Android media callbacks.

The manifest identifies `GameActivity` as launcher, enables splash launch, and
sets automatic splash removal to false. This is a direct match for the
Lightspeed image, but not for the visible SystemUI `Wait` dialog.

Unrelated loading resources from packaged SDK UI were not correlated to the
Phase15H visual and are not promoted as candidates.

```text
APK_WAIT_UI_MATCH = YES_FOR_LIGHTSPEED_SPLASH; NO_FOR_SYSTEM_WAIT_DIALOG
ANDROID_WAIT_UI_CREATOR = GAMEACTIVITY_ONCREATE
ANDROID_WAIT_UI_EXIT_METHOD = GAMEACTIVITY_ANDROIDTHUNKJAVA_DISMISSSPLASHSCREEN
ANDROID_WAIT_UI_EXIT_CONDITION = EXPLICIT_DISMISS_FLAG; NATIVE_TRIGGER_UNKNOWN
```
