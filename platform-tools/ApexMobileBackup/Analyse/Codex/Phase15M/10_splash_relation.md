# Splash relation

The resolved success neighborhood was limited to `FUN_059efe80`,
`FUN_059efff8`, and `FUN_059efe00` plus their direct graphics imports. It
contains no direct call, lookup, or reference to
`AndroidThunkJava_DismissSplashScreen`.

The failure neighborhood likewise exposes no proven splash-dismiss call. The
JNI environment helper immediately after the protected MessageBox target has
no statically identifiable dismissal semantics.

This agrees with Phase15I: Java exposes an explicit dismissal function, but
the native condition that invokes it is unresolved.

```text
GRAPHICS_SUCCESS_CONTROLS_SPLASH_DISMISS = NO_EVIDENCE
NATIVE_SPLASH_DISMISS_TRIGGER = UNKNOWN
```
