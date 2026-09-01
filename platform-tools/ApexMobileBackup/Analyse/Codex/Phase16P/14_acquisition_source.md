# Acquisition source

The readable `DolphinUpdater::CheckUpdate` continuation begins after a
protected or non-disassemblable prelude and already expects `owner+0x1f0` to
contain a valid interface. Neither the bounded owner methods nor the exact
callback table expose its non-null assignment.

```text
REAL_DOLPHIN_ACQUISITION_SOURCE = UNKNOWN_PROTECTED_OR_STATICALLY_OPAQUE_PRELUDE
REAL_DOLPHIN_ACQUISITION_REACHABLE = UNKNOWN
```

No dead `CreateDolphin` reference was used to fill this gap.
