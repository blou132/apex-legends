# Probe build

The SDK has no local NDK or CMake installation. The allowed minimal Java EGL
alternative was therefore built entirely with already installed tools:

| Component | Local version/path |
|---|---|
| JDK | `javac 21.0.10` |
| Android platform | `platforms/android-36.1/android.jar` |
| Build Tools | `36.1.0` |
| AAPT2 | `2.20-14042983` |
| D8 | `9.0.3-dev` |

`build-local.ps1` compiles Java, creates DEX, packages, aligns, signs with a
local disposable debug key, and verifies the APK. The final local-only APK is
`16,787` bytes with SHA-256
`A9D0ACA649F5802F678F3404219842B8325D0EC257D1FFC11819DFDDD1802682`.

Compiled-manifest inspection confirms package `local.graphicsprobe`, requested
GLES feature `0x00030001`, and no permission declaration. A case-insensitive
source scan found zero occurrences of `Apex`. Build result: `SUCCESS`; no
network dependency was used.
