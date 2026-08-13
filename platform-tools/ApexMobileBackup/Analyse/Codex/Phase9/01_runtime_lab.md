# Runtime lab audit

## Client artifacts

The recursive search of ignored `LocalInputs` and historical backup directories initially found no complete `base.apk`. The first ADB attempt also found no device. After the preserved phone was explicitly reconnected, the allowed read-only fallback recovered only `base.apk` into ignored local storage. No further phone command was issued.

Consequences:

- APK size: `96,228,800` bytes
- APK SHA256: `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0`
- ZIP entries: `978`
- complete APK native ABI set: `arm64-v8a` only
- native libraries: `17`, including one `lib/arm64-v8a/libUE4.so` entry of `190,192,944` uncompressed bytes

The APK inventory independently confirms the prior AArch64 analysis and establishes that an ARM64-capable lab is required.

The existing OBB copies were rehashed without modification:

| Artifact | Size | SHA256 |
| --- | ---: | --- |
| main OBB | 1,942,013,346 | `104B313F86A57F1B2A11D81D87E916EFBAB99BBBFB4A031E2D12B0201D5244E0` |
| patch OBB | 1,837,582,506 | `BFE887744EC21CB7CE0829F1C1F1BCAF1C400C358185B52D140D75242264973D` |

These values match Phase7C Resume.

## Installed Android environment

The host audit found:

- Android Emulator executable: present
- configured AVDs: `0`
- installed system images: one `x86_64` Google Play image
- installed ARM64 system image: none found
- running emulator endpoints: `0`
- connected secondary Android endpoints: `0`

No multi-gigabyte SDK component or system image was installed. An unconfigured x86_64 image is not treated as proof that this ARM64 native client can run.

## Isolation and network

No lab instance existed, so no app-specific network sandbox was configured. No client process ran and no external service was contacted.

```text
DYNAMIC_LAB_AVAILABLE = NO
DECISION_GATE = E DYNAMIC_LAB_UNAVAILABLE
```
