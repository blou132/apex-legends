# Dynamic table construction

A dynamically assembled registration table can only be traced after identifying
the constructing function or the `RegisterNatives` consumer. Neither boundary
is resolved in Phase12.

No `malloc`, stack array, `memcpy`, sequential store, string-pointer assignment,
or function-pointer assignment is classified as GameActivity registration
without that data-flow connection.

```text
DYNAMIC_TABLE_CONSTRUCTION = UNKNOWN
NAME_POINTER_SOURCE = UNKNOWN
SIGNATURE_POINTER_SOURCE = UNKNOWN
FUNCTION_POINTER_SOURCE = UNKNOWN
```
