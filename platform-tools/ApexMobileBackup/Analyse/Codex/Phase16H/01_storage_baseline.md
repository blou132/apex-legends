# Storage baseline

The bounded baseline covered `/data`, `/data/data`, `/data/user`, `/data/app`,
`/data/local`, `/data/local/tmp`, and shared storage. Pseudo-filesystems were
not traversed.

```text
DATA_FREE_BEFORE_BYTES = 7203102720
SHARED_FREE_BEFORE_BYTES = 7182131200
DATA_USED_BEFORE_BYTES = 2643009536
FIVE_GIB_FREE_TARGET_ALREADY_MET = YES
```

Largest measured top-level areas were `/data/app` at 79428 KiB,
`/data/media` at 67076 KiB, `/data/data` at 37580 KiB, and `/data/local` at
15432 KiB. The five-GiB operational headroom target was already met, so no
aggressive debloat was justified.
