# UE4 asset correlation

The Lightspeed visual has a complete APK/DEX explanation: an APK raw video is
played by an Android `VideoView` before the splash is dismissed. No UE4 asset
is needed to explain it.

Targeted reuse of prior Phase7/8 asset metadata found no matching PAK/OBB
Lightspeed, splash, loading-screen, or wait asset that supersedes the confirmed
APK resource. No broad asset scan was repeated.

The Android SystemUI ANR and the later Apex application dialog are also Android
windows, not confirmed UE4-rendered assets.

```text
UE4_SPLASH_ASSET_CANDIDATE = NO_EVIDENCE_APK_VIDEO_CONFIRMED
UE4_WAIT_UI_ASSET_CANDIDATE = NO_EVIDENCE_ANDROID_DIALOGS_CONFIRMED
```
