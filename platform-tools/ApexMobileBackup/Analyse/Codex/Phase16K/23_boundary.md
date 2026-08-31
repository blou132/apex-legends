# Static boundary

The bounded axis stops at:

```text
libUE4.so exact CreateDolphin import
  -> one syntactic direct call
  -> callsite invalidated as semantic owner by incompatible ABI/dataflow
  -> no returned-object store
  -> no provenance-qualified Init dispatch
  -> selector unavailable
```

The isolated preceding-PLT reference is a one-instruction malformed/data-like
function with no caller. It cannot repair the chain. Broad decompilation,
arbitrary indirect-call scans, and speculative class naming were not used.

```text
CURRENT_BLOCKER = CLIENT_DOLPHIN_INIT_DISPATCH_AND_SELECTOR_UNRESOLVED_AFTER_INVALIDATED_STATIC_IMPORT_CALL
STATIC_BOUNDARY = CLIENT_DISPATCH_TABLE_OR_TRANSFORMED_CALLSITE_UNRESOLVED
FINAL_GATE = E STATIC_AXIS_EXHAUSTED_RUNTIME_OR_STATE_COMPARISON_REQUIRED
```
