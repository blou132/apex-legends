# Splash exit conditions

| Condition | Evidence | Owner | Confidence |
| --- | --- | --- | --- |
| Splash launch enabled | Manifest metadata enables splash creation | Android `GameActivity` | Confirmed |
| Automatic removal disabled | Manifest metadata sets auto-remove false | Android `GameActivity` | Confirmed |
| Video prepared | Listener configures scaling and non-looping playback | Android media callback | Confirmed static path |
| Video completed | Listener sets the video-end flag and calls `nativeSplashVideoCompleted()` | Android to native bridge | Confirmed static path |
| Video completion dismisses splash | Conditional only when auto-remove is true; false in this build | Android `GameActivity` | Confirmed not sufficient |
| Explicit Java dismissal | Thunk sets dialog-end and invokes private dismissal | Android `GameActivity` | Confirmed exit function |
| Native condition invoking dismissal | No resolved native caller or state predicate | UE4/native | Unknown |
| TDM or GCloud success | No direct edge to splash dismissal | SDK/network | No evidence |
| Login, Lua, or server-list readiness | Names exist elsewhere but no direct edge to dismissal | Client/native | Unknown |

The private dismissal stops the video, dismisses the dialog, clears its
reference, and removes the splash-start preference when the dialog-end flag is
set. It does not itself require the video-end flag.

The existing read-only Ghidra program exposes the native completion export name
but does not resolve its body or a caller of the Java dismissal thunk. No exact
native exit condition can therefore be claimed.
