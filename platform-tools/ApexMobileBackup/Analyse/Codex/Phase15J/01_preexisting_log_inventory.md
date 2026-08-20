# Preexisting log inventory

Before Apex was launched, the package public tree contained seven files. Five
were official SDK log candidates:

| SDK category | Files | Type | Readable |
| --- | ---: | --- | --- |
| GCloudCore | 2 | Text log | Yes |
| GCloud | 2 | Empty text log | Yes |
| MSDK | 1 | XLog container | Yes |

The other two public files were the existing validation cache and SDK config.
No private application directory and no unrelated shared-storage path was
enumerated.

The five files were copied read-only to ignored local storage. Their raw names,
contents, sizes, and timestamps are not published.

```text
PUBLIC_SDK_LOG_DIRECTORY_PRESENT = YES
PUBLIC_SDK_LOG_FILE_COUNT = 5_PRELAUNCH
PREEXISTING_LOG_FILES_FOUND = 5
OFFICIAL_FILE_LOGS_READABLE = YES
```
