# Immediate selection gates

Without a proven client-side Init callsite, there is no bounded predecessor
block from which to classify null checks, flags, enum comparisons, or return
code gates. The post-extraction callback has a valid owner-null check, but it
guards callback forwarding and is not evidence of Dolphin bootstrap selection.

```text
DOLPHIN_INIT_IMMEDIATE_GATES = UNKNOWN; no proven client Init predecessor
```
