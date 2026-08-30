# Preserved input integrity

The three local inputs were independently hashed before use and matched the
authoritative Phase16I values:

| Input | Size | SHA-256 |
| --- | ---: | --- |
| APK | 96,228,800 | `2CC7253D7E81ACC9C0E7A9383CBD8C81C4311637F3EAFCD69CBDAD748F7C34C0` |
| main OBB | 1,942,013,346 | `104B313F86A57F1B2A11D81D87E916EFBAB99BBBFB4A031E2D12B0201D5244E0` |
| patch OBB | 1,837,582,506 | `BFE887744EC21CB7CE0829F1C1F1BCAF1C400C358185B52D140D75242264973D` |

APK metadata was package `com.ea.gp.apexlegendsmobilefps`, version code
`64003140`, version name `1.3.672.546`, and ABI `arm64-v8a`. Android signature
verification passed for the preserved APK. Nothing was modified or resigned.

```text
APK_HASH_EXACT = YES
MAIN_OBB_HASH_EXACT = YES
PATCH_OBB_HASH_EXACT = YES
APK_METADATA_EXACT = YES
APK_UNMODIFIED = YES
OBBS_UNMODIFIED = YES
```
