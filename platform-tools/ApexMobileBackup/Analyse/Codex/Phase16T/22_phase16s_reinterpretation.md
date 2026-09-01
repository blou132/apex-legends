# Phase16S reinterpretation

Phase16S found only the direct Shutdown pointer because, within the recovered
group, Shutdown is the only known DolphinUpdater method represented. The
secondary table does hide a second representation behind an adjustor thunk,
but that thunk converges to Shutdown itself rather than another lifecycle
method.

`PHASE16S_DIRECT_POINTER_FAILURE_REASON = ONLY_SHUTDOWN_IS_VIRTUAL`

This classification is bounded to the recovered group and does not claim
that every unrecovered DolphinUpdater method is globally non-virtual.
