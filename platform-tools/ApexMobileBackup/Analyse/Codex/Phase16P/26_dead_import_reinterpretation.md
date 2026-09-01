# Dead CreateDolphin import

The Phase16O direct `CreateDolphin` call remains statically unreachable and is
not an owner, acquisition, or runtime target. It retains only neighboring ABI
and producer context.

```text
DEAD_CREATEDOLPHIN_IMPORT_ROLE = LIFECYCLE_NEIGHBOR_ONLY
```
