# Storage audit

The first observation showed approximately `246 MiB` free on `/data` and
`226 MiB` on shared storage. The final exact readings were `443032 KiB`
(`432.6 MiB`) and `422552 KiB` (`412.6 MiB`) respectively. No cleanup command
was issued; the increase occurred through device background housekeeping during
the read-only audit.

Safe shared-storage candidates were effectively empty:

| Category | Size |
| --- | ---: |
| Download | 4 KiB |
| DCIM | 12 KiB |
| Pictures | 4 KiB |
| Movies | 4 KiB |
| Android/data | 13672 KiB |
| Android/obb | 3694664 KiB, protected Apex content |
| Huawei shared folder | 11228 KiB |

`pm list packages -3` returned only Apex. Android diskstats reports only about
`3.81 MiB` total application cache, so cache removal could not materially close
the gap and would risk violating the Apex exclusion.

The largest remaining reported consumers are Apex (`4.13 GB`, protected),
Google Play services (`746.6 MB`, system component), Google Search
(`687.7 MB`, system component), Google Photos (`249.5 MB`), and Google Maps
(`210.7 MB`). Phase15S did not alter any of them because the approved cleanup
scope did not authorize destructive changes to preinstalled system software.

```text
MINIMUM_FREE_SPACE = 2 GiB
FREE_SPACE_TARGET_REACHED = NO
SAFE_FREE_SPACE_TARGET_NOT_REACHED = YES
DELETIONS_PERFORMED = NONE
```
