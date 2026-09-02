# Scope

Phase16X is a PC-only, static, read-only analysis of the exact preserved
`libUE4.so`. It follows two bounded anchors: the `GameUpdateMgr` selector used
by `SkipAppUpdate`, and the selected `GameUpdateMgr` object's `+0x38` owner
field.

No phone, ADB, application runtime, ptrace, breakpoint, network, AppData,
binary modification, writable Ghidra operation, generic displacement scan, or
GameProtector analysis was used.
