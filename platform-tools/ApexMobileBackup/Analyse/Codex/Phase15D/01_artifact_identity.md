# Artifact identity

All three local-only inputs were hashed again before installation. Their byte
sizes and SHA256 values exactly match the Phase15D requirements.

| Artifact | Bytes | SHA256 | Result |
| --- | ---: | --- | --- |
| `base.apk` | 96228800 | `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0` | MATCH |
| `main.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1942013346 | `104B313F86A57F1B2A11D81D87E916EFBAB99BBBFB4A031E2D12B0201D5244E0` | MATCH |
| `patch.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1837582506 | `BFE887744EC21CB7CE0829F1C1F1BCAF1C400C358185B52D140D75242264973D` | MATCH |

The original files remain local-only and are not tracked by Git.
