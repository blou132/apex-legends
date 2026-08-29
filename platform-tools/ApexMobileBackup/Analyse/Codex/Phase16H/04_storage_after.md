# Storage after cleanup

The immediate post-clean measurement was:

```text
DATA_FREE_AFTER_BYTES = 7236706304
SHARED_FREE_AFTER_BYTES = 7215734784
TOTAL_BYTES_RECOVERED = 33603584
FUTURE_APEX_5GIB_HEADROOM = YES
```

After the trace-tool test and full temporary-file cleanup, the final observed
values were 7235137536 bytes free on `/data` and 7214166016 bytes free on
shared storage. Normal Android activity accounts for the small difference from
the immediate post-clean snapshot. Both measurements remain above five GiB.
