# Init argument provenance

Phase16J established the callee-side validation of callback, configuration,
path/input, and update type. Phase16K did not prove the client dispatch that
supplies those arguments, so no caller-side source can be assigned.

| Input | Client provenance |
|---|---|
| Interface object | Unknown |
| Callback | Unknown |
| Configuration | Unknown |
| Path/input object | Unknown |
| Update type | Unknown |

```text
DOLPHIN_INIT_ARGUMENT_PROVENANCE = UNKNOWN; client Init dispatch unresolved
```
