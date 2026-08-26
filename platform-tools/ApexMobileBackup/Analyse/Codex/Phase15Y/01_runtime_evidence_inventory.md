# Runtime evidence inventory

The following pre-existing Phase15U artifacts were used. Filenames are generic
and all files remain under the gitignored local evidence tree.

| Artifact | Purpose | Size | SHA-256 |
| --- | --- | ---: | --- |
| `logcat_raw.log` | bounded runtime timeline and exact anchors | 25,674,530 | `1ef6a4bcc1bb80e75f1710452b8cffe7cc27e55cfa4189e10f8ad097b0beb986` |
| `launch.txt` | unique launch/t0 corroboration | 229 | `da161de41a95c2ce989eeffb8897fe6d8d31d691ba679cef90eba436c800af68` |
| `run_summary.txt` | one-launch and stop-state corroboration | 295 | `3d2265c1c36a001333c42e508515ed668ebc0120be66592c370147988da878bf` |
| `screen_120s.png` | visible `2/17` and `I54140714` witness | 1,128,362 | `1f664f04c4c5dfb98bea51cfae72293f05b682ef8edf89990d04b1f7a04d9657` |
| `uiautomator.xml` | confirms no Android-view text anchor | 785 | `df3341f36aa673c8f46d71950376a373cd8449e4de7b5e7aa86109973a256daf` |

The raw log contains historical records from earlier dates. Only the bounded
Phase15U interval was searched for runtime conclusions.

```text
PHASE15U_RAW_EVIDENCE_FOUND = YES
RAW_EVIDENCE_COMMITTED = NO
```
