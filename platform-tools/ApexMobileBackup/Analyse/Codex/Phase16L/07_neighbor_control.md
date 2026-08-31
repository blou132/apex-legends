# Neighbor controls

| Relocation index | Symbol | PLT stub | Decoded GOT cell |
|---:|---|---:|---:|
| 528 | `IGCloud::GetInstance` | `0x0a3c18e0` | `0x0b37e8c0` |
| 529 | `CreateDolphin` | `0x0a3c18f0` | `0x0b37e8c8` |
| 530 | `DolphinHelper::GetCurApkPath` | `0x0a3c1900` | `0x0b37e8d0` |
| 531 | `ReleaseDolphin` | `0x0a3c1910` | `0x0b37e8d8` |

Each decoded GOT cell matches its exact relocation. The four earlier dummy
stubs alter the mathematical physical index but do not shift symbol
attribution in this neighborhood.

```text
PLT_NEIGHBOR_CONTROL_COUNT = 3
PLT_INDEX_MAPPING_CONSISTENT = YES
PLT_ATTRIBUTION_SHIFT = NONE
```
