# Splash relation

No Apex runtime occurred, so Phase15K adds no edge between dialog creation and
`AndroidThunkJava_DismissSplashScreen()`.

Phase15I remains authoritative: the Java dismissal function is confirmed, but
its native caller and state predicate are unresolved.

```text
DIALOG_CONTROLS_SPLASH_DISMISS = UNKNOWN
NATIVE_SPLASH_DISMISS_TRIGGER = UNKNOWN
NEW_SPLASH_RUNTIME_EVIDENCE = NO
```
