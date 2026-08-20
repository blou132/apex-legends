# OBB path and validation

## Expected path and identity

`Helpers.getSaveFilePath` (`0x5072a358`) builds the production directory from
`Environment.getExternalStorageDirectory()`, `/Android/obb/`, and the package
name. The resulting Phase15D path is:

```text
/storage/emulated/0/Android/obb/com.ea.gp.apexlegendsmobilefps
```

`OBBData` (`0x5058dbec`) declares exactly two entries:

| Kind | Expected filename | Expected bytes | Phase15D |
| --- | --- | ---: | --- |
| Main | `main.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1,942,013,346 | match |
| Patch | `patch.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1,837,582,506 | match |

The alternate development path is the shared-storage `/obb/<package>` path;
Phase15D selected the normal production path.

## Checks performed

The validation has three layers:

1. `doesFileExistInternal` (`0x50729fcc`) requires file existence and exact
   declared length.
2. `expansionFilesUptoData` (`0x5058d340`) optionally accepts a previous
   successful validation when `cacheFile.txt` contains matching file names and
   last-modified values.
3. On a cache miss, `DownloaderActivity$1.doInBackground` (`0x5058c4d4`)
   opens each OBB as a ZIP, enumerates entries, reads each entry with a recorded
   CRC in 256 KiB chunks, computes CRC32 over uncompressed bytes, and compares
   it with the ZIP entry value.

No whole-file MD5, SHA, or signature check appears in this path. There is no
separate manifest/resource-marker check, mount operation, or file-descriptor
validation. Version binding is performed by the fixed version in the expected
filename.

The identical OBB bytes passed this validator in Phase10. Phase15D proves
outer names/sizes and entry into the same full validator, but did not capture
its completion inside the bounded run.

```text
EXPECTED_OBB_DIRECTORY = /storage/emulated/0/Android/obb/com.ea.gp.apexlegendsmobilefps
MAIN_NAME_MATCH = YES
PATCH_NAME_MATCH = YES
VERSION_CODE_MATCH = YES
DIRECTORY_MATCH = YES
FILE_EXISTENCE_REQUIRED = YES
FILE_SIZE_CHECK = YES
HASH_CHECK = PER_ZIP_ENTRY_CRC32_ONLY
CONTENT_CHECK = YES
VERSION_CHECK = EXPECTED_FILENAME_VERSION_64003140
OBB_VALIDATION_EXPECTED = SUCCESS_FOR_IDENTICAL_PHASE10_BYTES
```
