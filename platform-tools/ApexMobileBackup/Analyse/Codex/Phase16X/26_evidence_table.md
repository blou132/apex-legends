# Evidence table

| Claim | Evidence | Result |
| --- | --- | --- |
| Exact input | SHA-256 and ELF metadata | Confirmed |
| Selector identity | Exact UTF-16 class-registration literals | `/Script/PureClient.GameUpdateMgr` confirmed |
| Lookup ABI | `FUN_04214458` plus `SkipAppUpdate` | Index, `-1`, and `0x18` stride confirmed |
| Value offset | Consumer load from element `+0x08` | Confirmed |
| Selected class | Exact Unreal inheritance check | Confirmed |
| Owner field | Consumer load from selected entry `+0x38` | Confirmed read |
| Exact key references | 25 functions / 74 materializations | 24 lookup, 1 accessor |
| Source mutation | Exact-key and same-collection filters | None found |
| Derived cache | `FUN_04214768`, `0x20` cache element | Rejected as different collection |
| Entry initializer | Exact class metadata callbacks | Not recoverable within boundary |
| Owner writer | Proven registration/initializer paths | Unknown |
| CheckUpdate layout | Owner `+0x1f0`, `+0x44`, call ABI | Consistent |
| Callback convergence | Producer precondition absent | Unknown |
