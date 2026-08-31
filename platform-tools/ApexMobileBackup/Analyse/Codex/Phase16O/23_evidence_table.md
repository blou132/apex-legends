# Evidence table

| Question | Evidence | Result | Confidence |
|---|---|---|---|
| Exact input | Two matching preserved copies and expected SHA256 | Valid AArch64 ELF | Confirmed |
| Function range | FDE plus raw executable bytes | `0x05a311a8...0x05a31ca8` | Confirmed |
| Factory identity | Manual BL decode, PLT, GOT, relocation, dynsym | `CreateDolphin` | Confirmed |
| Raw reachability | 206-block local CFG | Target raw-reachable | Confirmed, syntax-only |
| Semantic reachability | Noreturn call at `0x05a3144c` | Target not entry-reachable | Confirmed |
| Side entry | No `BR`, jump table, direct reference, or LSDA landing pad | None | Confirmed |
| Post-call target | Manual BL decode and relocation chain | `std::__throw_length_error(char const*)` | Confirmed |
| Result use | `x0 -> x1`; actual throw ignores `x1` | Not semantically used | Confirmed |
| Upper relation | Bounded direct caller chain and callback identifier | Post-extraction path handling | Confirmed |
| Dead-byte origin | Systematic adjacent-PLT semantic mismatch | Origin unknown | Unknown |
| Bootstrap owner | Dead import reference supplies no owner | Unresolved | Unknown |
