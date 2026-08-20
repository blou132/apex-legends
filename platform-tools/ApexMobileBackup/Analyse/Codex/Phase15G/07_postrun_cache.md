# Post-run validation cache

After the success transition, the same targeted read-only cache check found
`cacheFile.txt`. It contained exactly one row for each expected OBB name.

Each cached `lastModified` value matched the corresponding OBB filesystem time
converted from seconds to Java milliseconds:

| OBB | Name match | Last-modified match |
| --- | --- | --- |
| Main | yes | yes |
| Patch | yes | yes |

This is an independent post-run confirmation of the success path identified by
the runtime markers. The cache was not edited, deleted, or timestamped by the
analysis.

```text
VALIDATION_CACHE_POSTRUN = PRESENT
VALIDATION_CACHE_CONTENT_VALID_POSTRUN = YES
```
