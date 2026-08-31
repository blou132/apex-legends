# Boundary

The bounded static axis ends at the sole real factory call:

```text
CreateDolphin call confirmed
  -> return copied to x1
  -> no persistent store
  -> noreturn exception helper
  -> no owner
  -> no Init dispatch
```

No broader indirect-call search is justified because it would lack factory
pointer provenance.

```text
CURRENT_BLOCKER = ONLY_REAL_CREATEDOLPHIN_CALLSITE_DISCARDS_RETURN_BEFORE_PERSISTENT_OWNERSHIP
FINAL_GATE = C REAL_CREATEDOLPHIN_CALLSITE_RESOLVED_OBJECT_FLOW_LOST
```
