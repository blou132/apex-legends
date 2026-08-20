# Existing AVD state

The existing AVD metadata was read without modification.

| Property | Result |
| --- | --- |
| AVD name | `ApexPhase9Lab` |
| System image | `system-images;android-36.1;google_apis_playstore;x86_64` |
| Image revision | 4 |
| Image ABI | `x86_64` |
| Translated ABI | `arm64-v8a` |
| Native bridge | `libndk_translation.so` |
| ARM64 ISA mapping | `x86_64` |
| Native bridge execution | enabled |

```text
ARM64_TRANSLATION_EXPECTED = YES
```

This confirms the Phase9B image-capability result. It does not prove that the
Apex APK will run, because the AVD was deliberately not booted in Phase15A.
