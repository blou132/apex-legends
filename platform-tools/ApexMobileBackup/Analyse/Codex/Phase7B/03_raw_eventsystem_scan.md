# Phase7B - Raw EventSystem scan

## Execution status

`NOT_RUN_INPUTS_NOT_FOUND`

The prerequisite was not met: none of the four historical PAKs was restored and validated. Therefore no ASCII, UTF-8, or UTF-16LE scan was run against a PAK.

```text
RAW_EVENTSYSTEM_SCAN_RAN = NO
EVENTSYSTEM_FULL_PATH_FOUND = UNKNOWN
EVENTSYSTEM_FRAGMENT_FOUND = UNKNOWN
EVENTSYSTEM_NOT_FOUND = UNKNOWN
```

The last value deliberately remains `UNKNOWN`: it would be invalid to claim absence from PAK files that were not available for scanning. There are no Phase7B hit offsets, context bytes, local entropy values, nearby strings, or entry-boundary observations.
