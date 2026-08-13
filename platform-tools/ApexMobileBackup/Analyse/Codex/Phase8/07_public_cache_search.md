# Phase8 - Public phone cache search

The package public-storage tree was accessible read-only. Its two Android path aliases refer to the same storage and were not counted twice.

## Inventory

Ninety-four public files were observed:

| Bucket | Files | Result |
| --- | ---: | --- |
| `files/ProgramBinaryCache` | 82 | about 54.8 MB; graphics/program binary cache, no candidate filename |
| SDK log directory | 8 | excluded; not copied or searched |
| `files/cacheFile.txt` | 1 | 127-byte text file with main/patch OBB references and timestamps only |
| `files/TGPA/.tgpacloud` | 1 | 226-byte JSON SDK feature switches |
| `cache/GCloud.config` | 1 | 162-byte opaque SDK config |
| package-root DAT | 1 | 56-byte opaque binary |

Only the four files of 56-226 bytes were pulled to ignored local storage. Their hashes and non-sensitive classifications are in `output/public_cache_search.json`. None contains `EventSystem`, `Script/`, `Client/`, `Tools/`, `.lua`, `mount`, `pak`, `chunk`, `asset`, `hash`, or `path`.

The 82-file program cache was not copied. One 53.5 MB top-level file dominates it and its naming/layout identifies a GLSL ES Android program-binary cache, not a virtual-file manifest.

```text
PHONE_PUBLIC_DATA_ACCESS = ALLOWED
PUBLIC_CACHE_MAPPING_USEFUL = NO
RAW_LOGS_COPIED = NO
```
