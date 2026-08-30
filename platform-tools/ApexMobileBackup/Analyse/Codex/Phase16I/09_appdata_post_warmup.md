# Post-warm-up AppData

After force-stop, the package-only snapshot contained 49 private regular files
and 9 external regular files. The prelaunch-to-warm-up diff created normal
first-run structures: GCloud/GCloudCore/GPMSDK logs, UE configuration and cache
files, SDK preference files, and four SQLite databases.

Only schema names were read from SQLite. Published table names are structural;
no rows or preference values were published. A distinct OBB-validation cache
could not be attributed confidently from filenames alone.

```text
APPDATA_POST_WARMUP_SNAPSHOT = SUCCESS
APPDATA_DIFF_0_TO_1 = 49_PRIVATE_AND_9_EXTERNAL_FILES_CREATED
OBB_VALIDATION_CACHE_IDENTIFIED = UNKNOWN
```
