# Android and UE4 lifecycle

Phase10 remains authoritative for the observed lifecycle:

1. `DownloaderActivity` validates the local expansion files.
2. `GameActivity::onActivityResult` receives success and sets
   `HasAllFiles=true`.
3. `GameActivity::onResumeBody` invokes `nativeResumeMainInit`.
4. Later Java lifecycle messages show that the native call returned.

The manifest identifies `GameActivity` as a `NativeActivity` and declares
`android.app.lib_name=UE4`. This strongly supports a UE4 native handoff, but
Phase11 did not map the Java method to one function in the exact imported
`libUE4.so`. Therefore `LIBUE4_LOADED` is retained as `PROBABLE`, not promoted
under the stricter Phase11 rule, and the runtime load base remains `UNKNOWN`.

No reached native edge distinguishes engine readiness, window/surface/EGL,
render initialization, audio, or input as the black-screen gate.
