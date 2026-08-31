# Producer reason

Phase16L left the producer reason unknown because it had resolved the import
edge but not entry reachability. Phase16O closes that gap: the first
`throw_length_error` ends the legitimate path before the factory block, and no
direct, indirect, jump-table, or exception edge re-enters it.

The strongest supported category is therefore `UNREACHABLE_DEAD_CODE`. This
classifies control-flow status, not the unknown origin or intent of the bytes.

```text
CREATEDOLPHIN_PRODUCER_REASON = UNREACHABLE_DEAD_CODE
```
