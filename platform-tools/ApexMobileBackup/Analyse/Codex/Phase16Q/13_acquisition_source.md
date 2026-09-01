# Acquisition source

Without a non-null writer, the acquisition cannot be classified as a direct
call result, virtual call result, wrapper, allocation, global interface,
service lookup, singleton field, function argument, or constructor injection.

No source is promoted from string proximity, ABI compatibility, dead code, or
the previously invalidated dead `CreateDolphin` callsite.

```text
ACQUISITION_GLOBAL_SOURCE = UNKNOWN
ACQUISITION_WRAPPER_CHAIN = NONE_PROVEN
```
