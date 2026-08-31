# Protector ownership timing

In the first retained Phase16M run, the target's explicit non-zero tracer state
is recorded after the Puffer role event, but no earlier tracer sampling exists.
The final watcher observed the Puffer correlation and existing GameProtector3
ownership in the same actionable observation interval.

Therefore the evidence neither proves ownership before the early GCloudCore
event nor proves a safe unowned interval after it.

`PROTECTOR_OWNERSHIP_RELATIVE_ORDER = SAME_OBSERVATION_INTERVAL`
