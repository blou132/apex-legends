# DolphinUpdater accessor

The primary compiled-in accessor is:

- Ghidra: `0x081b12a0`
- ELF: `0x080b12a0`
- UClass cell: Ghidra `0x0b7bebe0`

An equivalent alias at Ghidra `0x05748b40` (ELF `0x05648b40`) uses the same
package, class literal, UClass cell, callbacks, size, flags, superclass, and
within-class arguments. The compiled-in pointer table references the primary
`0x081b12a0` accessor.

Both bodies therefore identify the same standalone
`/Script/PureClient.DolphinUpdater` UClass registration.
