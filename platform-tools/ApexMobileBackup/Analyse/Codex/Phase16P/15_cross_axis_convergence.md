# Cross-axis convergence

The callback methods identify their `+0x08` target semantically as
`DolphinUpdater`. Independently, exact `DolphinUpdater::Shutdown` and
`CheckUpdate` anchors operate on persistent interface field `+0x1f0`.

This supports a same-owner relationship, but it is not upgraded to confirmed
because callback non-null assignment and owner RTTI/vtable remain unresolved.

```text
CALLBACK_RELEASE_OWNER_RELATION = SAME_OBJECT_PROBABLE
CONVERGENCE_CONFIDENCE = PROBABLE
```
