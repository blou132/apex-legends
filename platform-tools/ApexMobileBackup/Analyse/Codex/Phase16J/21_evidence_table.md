# Evidence table

| Claim | Evidence | Strength |
|---|---|---|
| Phase15U entered CVersionMgr/Dolphin version bootstrap | Relative log sequence includes Init, NormalConnectVersionSvr, result, and error path | Direct |
| Phase16I warm-up reached Puffer but not the observed Dolphin path | 150-second bounded raw log with multiple Puffer retries and no target strings | Strong negative observation |
| Phase16I breakpoint was early enough | Install occurred near process start, before the historical +118 s window | Direct timing |
| Thread-10 executed historical Init | Same-run local TID maps the Init witness to `Thread-10` | Direct |
| Dolphin Init constructs and initializes CVersionMgr | Read-only exact-ELF vtable/caller chain | Direct static |
| Init and CheckAppUpdate are separate | Separate manager vtable slots and wrappers | Direct static |
| Puffer alone does not trigger Init | Phase16I Puffer retries without Dolphin/Init plus separate static caller chain | Strong |
| AppData controls the branch | Candidate structures exist, but no Phase15U comparison or direct static read | Unknown |
| Warm-up consumed Init before trace | Warm-up itself has no Init witness | Unsupported |
| Runs selected different bootstrap branches | Full Phase15U target path versus Phase16I Puffer-only path | Strong |
