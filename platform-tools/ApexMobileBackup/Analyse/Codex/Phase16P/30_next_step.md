# Next step

The next bounded task is to recover the non-null assignment to
`DolphinUpdater+0x1f0` from owner construction or the protected CheckUpdate
prelude using PC-only static evidence. It must not reopen the dead
`CreateDolphin` callsite or authorize runtime tracing.

```text
CURRENT_BLOCKER = NON_NULL_WRITER_FOR_DOLPHINUPDATER_PLUS_0X1F0_IS_PROTECTED_OR_STATICALLY_OPAQUE
NEXT_STEP = PC_ONLY_BOUNDED_OWNER_CONSTRUCTION_AND_FIELD_WRITE_PROVENANCE
FINAL_GATE = C RELEASEDOLPHIN_OWNER_FIELD_RESOLVED_ACQUISITION_UNKNOWN
```
