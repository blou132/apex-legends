# Storage result

| Checkpoint | `/data` available | Shared available |
| --- | ---: | ---: |
| Before cleanup | 442952 KiB (432.6 MiB) | 422472 KiB (412.6 MiB) |
| Final recorded checkpoint | 2121612 KiB (2.023 GiB) | 2101132 KiB (2.004 GiB) |

The `/data` baseline-to-final delta is `1678660 KiB`, approximately
`1639.3 MiB` or `1.601 GiB`. The final `/data` value is `24460 KiB` above the
2 GiB threshold.

`PHONE_STORAGE_READY = YES` at the recorded checkpoint. The small margin means
a later runtime phase should recheck free space immediately before launch and
avoid background updates or downloads. Phase15T itself did not launch Apex.

`PACKAGES_REMOVED = 8` and `CACHES_CLEARED = 0`.
