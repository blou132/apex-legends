# Class registration ABI

`FUN_081b1348` calls the shared helper at Ghidra `0x04763790` (ELF
`0x04663790`) from Ghidra `0x081b13e8` (ELF `0x080b13e8`). The accepted map
below uses caller-side arguments. The broader helper export is excluded;
see [30_boundary.md](30_boundary.md).

| Role | Value |
| --- | --- |
| Package | `/Script/PureClient` |
| Class name | `GameUpdateMgr` |
| UClass result cell | `0x0b7bec20` |
| Native-registration callback | `0x0a2aed54` |
| Size / alignment | `0x60` / `0x08` |
| Class flags / cast flags | `0x10000000` / `0` |
| Config-like argument | opaque encoded storage at `0x025c681e` |
| Constructor callback | `0x081abe34` |
| VTable helper callback | `0x0a2aed58` |
| AddReferencedObjects callback | generic stub `0x083e660c` |
| Superclass accessor | `0x07a43ac8` |
| Within-class accessor | `0x0647eb28` |
| Trailing byte flag at caller `sp+0x30` | `0` |

The shape is consistent with the legacy/private Unreal class-body
registration ABI. Callback role names are structural interpretations;
the argument values are observed directly. Exact source-version field names
and the meaning of the trailing flag are not forced.
