# Selection value backward slice

No selection value was recovered because no predicate reaching a proven Init
dispatch was found. The bounded trace stops at the unresolved client dispatch
rather than assigning semantics to unrelated callback state.

```text
DOLPHIN_SELECTION_VALUE_SOURCE = UNKNOWN
DOLPHIN_SELECTION_COMPARISON = UNKNOWN
DOLPHIN_SELECTION_TRUE_MEANS = UNKNOWN
```
