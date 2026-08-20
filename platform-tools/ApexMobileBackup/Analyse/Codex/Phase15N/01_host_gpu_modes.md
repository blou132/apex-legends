# Host and GPU mode inventory

`emulator.exe -accel-check` exited with code `0` and reported
`WHPX(10.0.26200) is installed and usable`.

The installed emulator is version `36.4.9.0` (build `14788078`). Its
`-help-gpu` output explicitly lists:

| Mode | Emulator description |
|---|---|
| `auto` | automatic recommended selection |
| `host` | host GPU drivers |
| `software` | default software renderer |
| `lavapipe` | Lavapipe for Vulkan and automatic software GLES |
| `swiftshader` | SwiftShader for GLES and Vulkan |
| `swangle` | ANGLE with SwiftShader for GLES |

`swiftshader_indirect` is not listed by this emulator and was not used. All six
listed modes were tested separately; no undocumented renderer combination was
attempted.

The required local image exists at
`system-images;android-36.1;google_apis_playstore;x86_64`. No SDK package or
system image was downloaded.
