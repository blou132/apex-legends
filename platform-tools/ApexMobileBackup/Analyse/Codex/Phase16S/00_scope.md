# Phase16S scope

Phase16S was a PC-only, static, read-only analysis of the preserved
`libUE4.so` and, for named ABI cross-checks only, `libgcloud.so`.

The analysis used exact pointers for four already-known DolphinUpdater methods,
bounded table inspection, and a Dolphin-only dynamic-symbol inventory. It did
not use a device, ADB, Apex, ptrace, breakpoints, AppData, network access, or
binary modification. Ghidra projects were opened with `-noanalysis -readOnly`.

The stop rule was applied: no broader pointer scan was performed after the
multi-method vtable recovery failed.
