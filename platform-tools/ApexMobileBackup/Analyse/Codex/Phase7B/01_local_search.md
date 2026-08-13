# Phase7B - Local search

## Search method

The search used exact filename matching in these local scopes:

- the current Apex repository;
- the `Downloads/platform-tools-latest-windows` tree;
- the Downloads tree;
- the Desktop tree;
- visible directories named `ApexMobileBackup` inside those scopes.

No byte-by-byte disk scan was performed.

## PAK result

The following names returned no candidate:

- `launch.pak`
- `pakchunk2-Android_ASTC.pak`
- `pakchunk3-Android_ASTC.pak`
- `pakchunk4-Android_ASTC.pak`

With no candidate file, no size, modification time, or SHA256 could be calculated.

## OBB fallback result

No `main*.obb` or `patch*.obb` file was found. The only visible `ApexMobileBackup` directory is the current repository tree, which contains no original PAK or OBB input.

```text
PAK_CANDIDATES_FOUND = 0
OBB_CANDIDATES_FOUND = 0
RESTORE_PERFORMED = NO
```
