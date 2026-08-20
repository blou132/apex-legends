# Existing AVD validation

`emulator.exe -list-avds` completed successfully and returned only
`ApexPhase9Lab`. Its configuration remains unchanged:

| Property | Result |
| --- | --- |
| AVD name | `ApexPhase9Lab` |
| System image | `system-images;android-36.1;google_apis_playstore;x86_64` |
| Image ABI | `x86_64` |
| ABI list | `x86_64,arm64-v8a` |
| Translated ABI | `arm64-v8a` |
| Native bridge | `libndk_translation.so` |
| ARM64 ISA mapping | `x86_64` |
| Native bridge execution | enabled |

```text
ARM64_TRANSLATION_EXPECTED = CONFIRMED YES
AVD_STARTED = NO
```

This validates configuration only. Guest boot and runtime translation remain
for a separately authorized phase.
