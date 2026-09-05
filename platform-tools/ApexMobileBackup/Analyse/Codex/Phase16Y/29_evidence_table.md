# Evidence table

| Claim | Evidence | Result |
| --- | --- | --- |
| Exact input | SHA-256 and ELF header | Confirmed |
| GameUpdateMgr class | Exact package/class accessor | Confirmed |
| GameUpdateMgr layout | Caller-side size/alignment | `0x60` / `0x08` |
| GameUpdateMgr superclass | Exact superclass accessor literals | `/Script/Basic.ApexGameInstanceSubsystem` |
| DolphinUpdater UClass exists | Exact package/class accessor and shared UClass cell | Confirmed |
| DolphinUpdater layout | Caller-side size/alignment | `0x430` / `0x08` |
| Compiled-in registration control | Adjacent accessor entries in one bounded table | Valid |
| GameUpdateMgr property array | No direct structural argument or reachable block | Unknown |
| `GameUpdateMgr+0x38` reflected | No class-scoped property metadata | Unknown |
| `+0x38` PropertyClass is DolphinUpdater | No PropertyClass edge | Unknown |
| GameUpdateMgr native functions | Registration callback opaque | Unknown |
| DolphinUpdater native functions | Registration callback opaque | Unknown |
| DolphinUpdater known-layout convergence | Class size covers `+0x1f0/+0x1f8` | Compatible |
| `+0x38` reflected setter | No exact reflected function names | Unknown |
| `+0x38` owner producer | No proven write path | Unknown |
| Object-at-`+0x38` class identity | Existing lifecycle plus new layout only | Probable |

## Provenance

The exact accepted accessor records and machine-address pairs are exported in
[class_accessor.json](output/class_accessor.json),
[class_registration.json](output/class_registration.json), and
[dolphinupdater_class.json](output/dolphinupdater_class.json).
[controls.json](output/controls.json) identifies the two bounded pointer
records. [identity.json](output/identity.json) links the inherited lifecycle
evidence and distinguishes it from newly recovered class metadata.

[gate.json](output/gate.json) pins the input and local export hashes.
Those hashes identify complete local files, including surplus output; only
the record selectors cited by the accepted exports support these conclusions.
The execution deviation and excluded material are documented in
[30_boundary.md](30_boundary.md).
