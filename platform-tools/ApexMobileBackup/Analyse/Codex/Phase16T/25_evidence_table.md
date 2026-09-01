# Evidence table

| Claim | Evidence | Confidence |
| --- | --- | --- |
| Shutdown cell is valid | RELA addend and Ghidra applied target agree | Confirmed |
| `-0x28` is offset-to-top | header geometry plus matching X0 adjustment | Confirmed |
| Secondary address point is `0x0ae568b0` | ABI header followed by executable entries | Confirmed |
| Primary address point is `0x0ae56608` | contiguous run and compatible zero typeinfo | Probable |
| Group belongs to DolphinUpdater | exact Shutdown plus secondary owner thunk | Probable |
| Constructor is recovered | no exact address-point references or stores | Not proven |
| New `+0x1f0` route exists | constructor absent | No |
