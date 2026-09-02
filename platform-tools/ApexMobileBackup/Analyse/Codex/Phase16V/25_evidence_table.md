# Evidence table

| Claim | Evidence | Confidence |
|---|---|---|
| Four external CheckUpdate callers | exact aligned `BL` targets to FDE start | Confirmed |
| CheckUpdate callers pass owner argument or selected field | bounded register slicing | Confirmed |
| OnNotice and FirstExtract use callback `+0x08` | exact loads, null tests, direct calls | Confirmed |
| Callback `+0x08` is persistent owner storage | two independent lifecycle uses | Confirmed location |
| Stored owner is DolphinUpdater | exact OnNotice identifier plus owner continuation semantics | Probable class identity |
| Callback owner field null initialization | exact `stp x8,xzr,[x0]` in initializer | Confirmed |
| Callback owner non-null assignment | no exact proven writer | Unknown |
| CheckUpdate/Shutdown shared storage | no external Shutdown caller | Unknown |
| New `+0x1f0` acquisition route | only reader/gate recovered | Not proven |
