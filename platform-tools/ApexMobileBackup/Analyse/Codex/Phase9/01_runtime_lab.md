# Runtime lab audit

## Client artifacts

The recursive search of ignored `LocalInputs` and historical backup directories found no complete `base.apk`. ADB then reported zero authorized devices, so the fallback read-only APK pull could not start.

Consequences:

- APK size: `UNKNOWN`
- APK SHA256: `UNKNOWN`
- complete APK ABI set in Phase9: `UNKNOWN`
- prior architecture evidence: `arm64-v8a`, from the previously analyzed `lib/arm64-v8a/libUE4.so` artifact and AArch64 Ghidra program

The prior evidence establishes that an ARM64-capable lab is required, but it does not replace a fresh inventory of every ABI in the unavailable APK.

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
