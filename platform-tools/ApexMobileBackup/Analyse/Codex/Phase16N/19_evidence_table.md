# Evidence table

| Question | Evidence | Result | Confidence |
|---|---|---|---|
| Duplicate names | Phase16M task snapshot | five `Thread-10` tasks | CONFIRMED |
| Selected role | same-run Puffer TID to task comm | `THREAD10_A` | CONFIRMED |
| Earliest event | same-TID backward log correlation | graphics event at +3.302 s | CONFIRMED |
| Earliest useful role event | same correlation | GCloudCore at +4.063 s | CONFIRMED |
| Unique in retained run | comparison of five instances | yes | CONFIRMED |
| Before protector | missing early tracer sample | unknown | UNKNOWN |
| Stable ordinal | single snapshot only | no | LOW |
| Cross-run role | Phase15U/16I/16M | repeated component sequence | MEDIUM |
| Static producer | exact ELF symbol and source anchor | service method | HIGH |
| Dedicated worker/start | bounded producer path | not proven | UNKNOWN |
| Direct CreateDolphin executor | no runtime trap | possible only | LOW |
| Ptrace compatibility | existing owner at actionable point | incompatible | HIGH |
