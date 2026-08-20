# Official file logs

## Snapshot comparison

| Snapshot | Official files | New files | Existing files grown |
| --- | ---: | ---: | ---: |
| Before launch | 5 | 0 | 0 |
| About +30 s | 8 | 3 | 0 |
| About +120 s | 8 | 3 | 0 |
| End, about +180 s | 8 | 3 | 0 |

One new GCloudCore text log, one empty GCloud text log, and one MSDK XLog file
were created in a new time bucket by +30 seconds. Their sizes did not change in
later snapshots. File creation is confirmed; renaming or rotating an existing
file was not directly observed.

The new GCloudCore file contains SDK setup, configuration, and offline request
failure messages. The new MSDK file contains the same generic SDK provider-list
class as the preexisting file. The new GCloud file is empty.

No file contains a confirmed Apex client stage or splash exit. Therefore new
official messages exist, but they are SDK-only.

```text
FILE_CREATED = YES_3
FILE_GROWN = NO
FILE_ROTATED = NO_DIRECT_EVIDENCE; NEW_TIME_BUCKET_FILES_CREATED
NEW_OFFICIAL_LOG_MESSAGES = YES_SDK_ONLY
```
