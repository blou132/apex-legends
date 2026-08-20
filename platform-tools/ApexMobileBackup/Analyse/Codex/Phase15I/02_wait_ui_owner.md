# Wait UI owner

## Layer classification

The Phase15H screen is a composition of Android windows, not one UE4 wait UI.

1. The immersive-mode tutorial belongs to `com.android.systemui`.
2. The visible `Wait` action belongs to an Android ANR dialog for SystemUI.
3. The Lightspeed logo is rendered by an Apex `GameActivity` Android
   `VideoView` inflated from the APK.
4. A later Apex-owned Android application dialog exposes an `OK` action. Its
   obscured message and creator cannot be identified from retained evidence.

The SystemUI ANR dialog was already drawn before the Apex splash started. It
therefore cannot be treated as the application's splash-exit control.

## Exit ownership

The APK splash is created by `GameActivity.onCreate`. The confirmed Java exit
entry point is `GameActivity.AndroidThunkJava_DismissSplashScreen()`, which sets
the dismissal flag and calls the private `DismissSplashScreen()` method.

The exact manifest sets automatic splash removal to false. Video completion
records completion and notifies native code, but does not dismiss the splash on
its own. A later explicit native-to-Java dismissal is required. The exact native
state or function that invokes that Java thunk remains unresolved.

```text
WAIT_UI_OWNER = ANDROID_CONFIRMED
WAIT_UI_CREATOR = GAMEACTIVITY_ONCREATE_FOR_SPLASH; ANDROID_SYSTEM_FOR_ANR_WAIT; UNKNOWN_FOR_APEX_OK_DIALOG
WAIT_UI_EXIT_FUNCTION = GAMEACTIVITY_ANDROIDTHUNKJAVA_DISMISSSPLASHSCREEN_CONFIRMED
WAIT_UI_EXIT_CONDITION = NATIVE_EXPLICIT_DISMISS_REQUIRED; EXACT_NATIVE_TRIGGER_UNKNOWN
```
