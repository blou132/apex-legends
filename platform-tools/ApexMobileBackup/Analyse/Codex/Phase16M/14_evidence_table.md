# Evidence table

| Question | Evidence | Result | Confidence |
|---|---|---|---|
| Repository/device preflight | clean Git; PRA-LX1 only; root; SELinux | passed | CONFIRMED |
| Network isolation | airplane on; radios off; no route | passed | CONFIRMED |
| Exact package inputs | APK and ELF hashes | exact | CONFIRMED |
| Runtime modules mapped | root `/proc` maps, local-only | both present | CONFIRMED |
| Load biases | PT_LOAD offset/VA derivation | resolved local-only | CONFIRMED |
| Runtime bytes | ptrace read gate not crossed | not read | UNKNOWN |
| Runtime GOT pointer | ptrace read gate not crossed | not read | UNKNOWN |
| Executor identity | same-run Puffer TID plus historical Dolphin role | `Thread-10` | PROBABLE |
| Executor availability | `/proc` tracer ownership | owned by `GameProtector3` | CONFIRMED |
| Hardware breakpoint | watcher stopped before SET | not programmed | CONFIRMED |
| Entry/caller/return/consumer | no active trap | unresolved | UNKNOWN |
| Cleanup | force-stop, artifact removal, process checks | passed | CONFIRMED |
