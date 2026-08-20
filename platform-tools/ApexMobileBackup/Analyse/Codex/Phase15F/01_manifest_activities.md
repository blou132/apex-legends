# Manifest activities

The binary manifest and `aapt2 dump badging` identify `GameActivity` as the
only Android launcher. Attributes not present on an activity use Android or
application defaults; they are reported as inherited defaults, not as explicit
manifest values.

| Field | GameActivity | DownloaderActivity |
| --- | --- | --- |
| Class | `com.epicgames.ue4.GameActivity` | `com.ea.gp.apexlegendsmobilefps.DownloaderActivity` |
| Exported | explicit `true` | default `false` (no intent filter) |
| Enabled | default `true` | default `true` |
| Launch mode | `singleTask` (`2`) | default `standard` |
| Task affinity | default package affinity | default package affinity |
| Theme | `@style/SplashTheme` (`0x7f0f0104`) | no activity/application theme declared; platform default |
| Config changes | `0x1fb3` | none declared |
| Intent filters | `MAIN` plus `LAUNCHER` | none |
| Main launcher | `YES` | `NO` |
| Process | default application process | default application process |

`0x1fb3` covers MCC, MNC, keyboard, keyboard-hidden, orientation,
screen-layout, UI-mode, screen-size, smallest-screen-size, and density changes.
`DownloaderActivity` has no separate `process`, affinity, orientation, theme,
or configuration override in the manifest.

The application metadata also states:

- `bPackageDataInsideApk=false`
- `bHasOBBFiles=true`
- `bVerifyOBBOnStartUp=true`
- `requestLegacyExternalStorage=true`
- target SDK 32

```text
GAMEACTIVITY_IS_LAUNCHER = YES
DOWNLOADERACTIVITY_IS_LAUNCHER = NO
```
