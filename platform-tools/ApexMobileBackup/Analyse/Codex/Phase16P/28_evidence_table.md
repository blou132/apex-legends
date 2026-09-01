# Evidence table

| Finding | Evidence | Status |
|---|---|---|
| Callback owner field is `+0x08` | exact success callback decompile | Confirmed |
| Callback table is `0x0af55170` | exact pointer cell and table construction | Confirmed |
| Callback owner is `DolphinUpdater` | exact forwarded owner method identifiers | Probable |
| Exact direct `ReleaseDolphin` callers | exact PLT-target branch scan | None |
| Owner Dolphin field is `+0x1f0` | Shutdown use/cleanup/clear and CheckUpdate readers | Confirmed |
| Non-null acquisition source | no readable proven writer | Unknown |
| Real client Init dispatch | owner field read, vtable slot `+0x10`, `BLR` | Confirmed |
| Concrete `GCloudDolphinImp` dynamic vptr | ABI match without assignment provenance | Probable |
| Puffer relation | no direct exact relationship | Not proven |

PLT adjacency, dead code, string proximity alone, and the noisy global
displacement scan were excluded from promotion.
