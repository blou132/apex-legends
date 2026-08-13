# Phase8 - OBB metadata inventory

## Complete non-PAK inventory

The extracted OBB trees contain 32 files: four PAK files and 28 files outside PAK containers. The 28 non-PAK files total `180356971` bytes and consist only of 26 MP4 videos and two JPG images under `MAIN/AClient/Content/Art/Movies/`. The patch extraction contains no non-PAK file.

| Relative group | Files | Bytes | Classification |
| --- | ---: | ---: | --- |
| `Rating/` | 2 | 4589260 | MP4 media |
| `S3_5CG/` | 4 | 28699622 | MP4 media |
| `S3CG/` | 2 | 73279476 | MP4 media |
| `S4CG/` | 3 | 17871537 | MP4 media |
| `SignatureWeapon/` | 1 | 36575303 | MP4 media |
| `skillbase/` | 3 | 9070364 | MP4 media |
| `Tutorial/` | 13 | 10251409 | 11 MP4 and 2 JPG media |

The exact 28 relative paths, sizes, and SHA256 values are recorded in `output/obb_metadata_inventory.json`. No file was opened as a candidate manifest because every non-PAK file has a media extension and matching media placement.

## Requested-name search

No non-PAK filename matched a manifest, asset registry, registry, PAK list, file list, index, mount, chunk, container, catalog, resource, config, cache, Lua, script, version, or patch metadata name.

## PAK sidecars

No `*.sig`, `*.ucas`, `*.utoc`, `*.manifest`, `*.idx`, `*.index`, `*.paklist`, or `*.bin` file exists beside or elsewhere in the extracted OBB trees.

```text
READABLE_EXTERNAL_ENTRY_INDEX = NO
PAK_SIDECAR_COUNT = 0
```

The only available PAK indexes remain the high-entropy internal index regions documented in `04_pak_analysis.md`; this phase did not decrypt or bypass them.
