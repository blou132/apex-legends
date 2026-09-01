# GCloudDolphin connection

The client dispatch remains ABI-compatible with the Phase16J
`GCloudDolphinImp::Init` interface and uses its known slot `+0x10`. That does
not identify the producer of the stored pointer.

Because the acquisition source is unresolved, the source cannot be connected
to an exact `GCloudDolphinImp` factory, wrapper, constructor helper, or vtable
installation. The dead Phase16O `CreateDolphin` callsite was not reopened.

```text
ACQUISITION_SOURCE_IS_GCLOUDDOLPHIN = UNKNOWN
```
