# Phase7C Resume - PAK recovery

The OBB copies were extracted locally into ignored storage after validating all archive paths. Four target PAK files were recovered.

| PAK | Source | Size | SHA256 | Size match |
| --- | --- | ---: | --- | --- |
| `launch.pak` | main OBB | 140867491 | `555FDC85BCF0F8DE88A9722F7E2B3EFC40D0E987F34B54037FF4891630BFB85E` | yes |
| `pakchunk2-Android_ASTC.pak` | main OBB | 367571213 | `F74548A3C43F4631E73139F685290DD31EAF6309E410F6C73C86CA9F6C627143` | yes |
| `pakchunk3-Android_ASTC.pak` | patch OBB | 1837582232 | `16332BCC7FE9EB41AA8DED2ECB3EC7DB4E968A2A74CF3E98824DD29969ECD77E` | yes |
| `pakchunk4-Android_ASTC.pak` | main OBB | 1253209707 | `568ED18831B039FCA0F61F196E7FF0C8A04A0F85998EF8CCC637E44C593D74A8` | yes |

All four sizes match the historical observations. The OBB identities, exact `ClientLaunch.lua` witness offset, and two `pakchunk3` witnesses jointly support build consistency.

```text
PAKS_RECOVERED = 4/4
ALL_HISTORICAL_SIZES_MATCH = YES
```
