# Duplicate Thread-10 instances

Five tasks had the exact comm name `Thread-10` in the retained Phase16M
snapshot. Numeric identifiers are replaced by stable report-local labels.

| Label | Observed order | First retained tag | GCloud evidence | Puffer evidence | Selected |
|---|---:|---|---|---|---|
| THREAD10_A | 1 | `mali_winsys` | YES | YES | YES |
| THREAD10_B | 2 | `GPMSDK` | YES_INTERNAL_ONLY | NO | NO |
| THREAD10_C | 3 | NONE_RETAINED | NO | NO | NO |
| THREAD10_D | 4 | NONE_RETAINED | NO | NO | NO |
| THREAD10_E | 5 | NONE_RETAINED | NO | NO | NO |

Observed order is numeric allocation order in one snapshot, not a proven
cross-run creation chronology. Thread name alone is not unique.

`THREAD10_INSTANCE_COUNT = 5`
