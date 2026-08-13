# Installed system image

## Identity

| Field | Value |
| --- | --- |
| package ID | `system-images;android-36.1;google_apis_playstore;x86_64` |
| API level | `36.1` (`ro.build.version.sdk=36`) |
| Android version | `16` |
| tag | `google_apis_playstore` |
| image family | Google Play |
| primary ABI | `x86_64` |
| package revision | `4` |
| security patch | `2026-01-05` |
| required emulator | `35.4.9` or newer |
| installed emulator | `36.4.9.0`, build `14788078` |

The image is the only installed system image. No SDK component or alternate image was downloaded during Phase9B.

## Source evidence

- `source.properties` supplies the package description, revision, API, tag, and ABI.
- `package.xml` supplies the exact package ID, display name, revision, and translated ABI metadata.
- `build.prop` supplies Android 16/API 36 runtime properties and native-bridge configuration.

Only cleaned properties are published; the multi-gigabyte image remains local SDK content.
