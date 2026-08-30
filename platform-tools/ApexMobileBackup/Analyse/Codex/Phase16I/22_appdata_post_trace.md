# Post-trace AppData

With Apex stopped, the package-only snapshot contained 49 private and 12
external regular files. Relative to the post-warm-up snapshot:

- 5 log files were created;
- 8 database, preference, mmap, or configuration files changed;
- 2 prior empty crash-log files were replaced;
- no unrelated package data was inspected.

Structural names indicate routine SDK, GCloud, measurement, crash-reporting,
and configuration state. No filename alone is promoted to a semantic update
result. Raw files, values, rows, hashes, and timestamps remain local-only.

```text
APPDATA_POST_TRACE_SNAPSHOT = SUCCESS
APPDATA_DIFF_1_TO_2 = CREATED_5_MODIFIED_8_DELETED_2
```
