# Owner flag set markers

## Set path 1

After the local conversion path succeeds, ELF `0x05a2f7c0` tests `W22.bit0`.
When set, the first-extract-needed path reaches an optional level-5 log and
then unconditionally stores one to `owner+0x45` at ELF `0x05a2f808`.

## Set path 2

The bounded loop has `W26 = 1`. At ELF `0x05a2fb8c`, a clear result bit enters
the missing-first-extract-file path. An optional level-2 log is followed by
the store of `W26` to `owner+0x45` at ELF `0x05a2fbd4`.

The messages prove their respective store path when observed, because no
conditional branch occurs between the returning log call and store. Logging
can be skipped by level checks, so absence never disproves the stores.

| Run | Set 1 marker | Set 2 marker |
|---|---|---|
| Phase15U | NOT_OBSERVED | NOT_OBSERVED |
| Phase16I warm-up | NOT_OBSERVED | NOT_OBSERVED |
| Phase16I trace | NOT_OBSERVED | NOT_OBSERVED |
| Phase16M | NOT_OBSERVED | NOT_OBSERVED |

```text
FLAG45_SET1_TRIGGER = VALID_FIRST_EXTRACT_PATH_AND_W22_BIT0_SET
FLAG45_SET1_OBSERVABLE_MARKER = FirstExtract Need first extract, Check last extract paks
FLAG45_SET2_TRIGGER = FIRST_EXTRACT_ENTRY_RESULT_BIT0_CLEAR_WITH_W26_EQ_1
FLAG45_SET2_OBSERVABLE_MARKER = UDolphinUpdater::FirstExtractPak, Missing first extract file: %s, Need first extract
```
