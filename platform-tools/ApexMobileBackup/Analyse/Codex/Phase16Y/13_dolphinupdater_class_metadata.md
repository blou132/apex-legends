# DolphinUpdater class metadata

The exact registration arguments provide:

- Package: `/Script/PureClient`
- Class: `DolphinUpdater`
- UClass cell: Ghidra `0x0b7bebe0`
- Class size: `0x430`
- Alignment: `0x08`
- Class flags: `0x10000004`
- Native-registration callback: `0x0a2aecd0` (opaque)
- Constructor callback: `0x081ab928` (opaque)
- VTable helper callback: `0x0a2aed4c` (opaque)
- AddReferencedObjects callback: generic stub `0x083e660c`

The reported size is greater than the highest proven fields `+0x1f0` and
`+0x1f8`, so the known runtime layout is compatible.

The superclass/within-class arguments both use the same root accessor. Its
literal storage is opaque on the authorized path, so the exact superclass
name is not forced.
