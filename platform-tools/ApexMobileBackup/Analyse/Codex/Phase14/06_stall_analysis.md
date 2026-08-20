# Stall analysis

Three comparable native snapshots are required to distinguish a persistent
wait from a transient frame. Phase14 obtained none, so no thread, function,
wait primitive, task-graph state, game-thread state, or render-thread state can
be classified.

The black screen remains an observed application symptom from the earlier
offline run. It is not sufficient to identify a native stall frame.

```text
FIRST_RUNTIME_STALL_FRAME = UNKNOWN
PERSISTENCE_ANALYSIS = UNAVAILABLE_NO_BACKTRACE
```
