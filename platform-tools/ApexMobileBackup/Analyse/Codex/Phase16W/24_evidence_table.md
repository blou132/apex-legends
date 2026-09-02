# Evidence table

| Claim | Exact evidence | Confidence |
|---|---|---|
| Provider Init slot and range | table cell `0x00979630`; Ghidra function body | Confirmed |
| Callback register is `X5` | entry null test and store to provider `+0x10` | Confirmed |
| Client callback source is owner `+0x1f8` | exact callsite register setup | Confirmed |
| Owner `+0x1f8` is callback pointer | provider ABI plus cleanup/read/clear lifecycle | Confirmed role |
| Concrete object uses known callback table | ABI matches, but no vptr store on instance | Probable |
| Callback `+0x08` null initialization | exact `stp x8,xzr,[x0]` | Confirmed |
| Callback `+0x08` non-null writer | no proven writer | Unknown |
| Selected entry `+0x38` owner consumer | exact bounded lookup and load | Confirmed |
| Selected entry `+0x38` producer | no proven writer | Unknown |
| Cross-storage pointer convergence | both producers missing | Unknown |
