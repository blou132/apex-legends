# Apex restore readiness

## PC-only artifact verification

The preserved, ignored PC artifacts still exist with the authoritative sizes:

| Artifact | Size (bytes) | Recorded SHA-256 prefix | State |
| --- | ---: | --- | --- |
| `base.apk` | 96228800 | `2CC7253D...` | PRESENT, gitignored |
| `main.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1942013346 | `104B313F...` | PRESENT, gitignored |
| `patch.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1837582506 | `BFE88774...` | PRESENT, gitignored |

The complete hashes and prior byte-for-byte verification are recorded in
`Phase15D/01_artifact_identity.md`. Standard APK and OBB installation steps are
documented in `Phase15D/03_apk_install.md` and `Phase15D/04_obb_install.md`.

## Phone state preserved before any future wipe

- Package: `com.ea.gp.apexlegendsmobilefps`.
- Version: `1.3.672.546`, code `64003140`.
- Package state: stopped.
- Main OBB: `1942013346` bytes.
- Patch OBB: `1837582506` bytes.
- Available `/data`: `2123708 KiB`.
- Android/EMUI/build: recorded in `01_device_firmware.md`.

No reinstall was performed.

```text
APEX_POST_WIPE_REINSTALL_READY = YES
```
