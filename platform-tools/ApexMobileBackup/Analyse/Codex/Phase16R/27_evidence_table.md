# Evidence table

| Claim | Result | Basis |
|---|---|---|
| W23 reads `bUseIFSFile` | CONFIRMED | Exact helper inputs and bounded decompilation |
| W23 value observable in retained logs | UNKNOWN | No helper marker |
| `+0x45` blocks Init | INVALIDATED | Both flag routes join the shared dispatch |
| `+0x45` selects first-extract Init mode | CONFIRMED | Alternate `x4` setup and shared call site |
| Callback entry proves flag clear | INVALIDATED | Log precedes owner null-check |
| Set marker 1 proves set path when observed | CONFIRMED | Marker immediately precedes store |
| Set marker 2 proves set path when observed | CONFIRMED | Marker immediately precedes store |
| Any flag marker observed in retained runs | NOT_OBSERVED | Exact retained-log searches |
| Phase15U provider Dolphin Init | CONFIRMED | One-way VersionMgr implication |
| Phase15U exact client dispatch | PROBABLE | Interface provenance/dynamic target unresolved |
| Phase16I Dolphin Init | NOT_OBSERVED | Complete bounded logs |
| AppData caused run divergence | UNSUPPORTED | No Phase15U baseline or accessor chain |
| Remote input caused run divergence | UNSUPPORTED | Same offline Puffer condition, different Init witness |
