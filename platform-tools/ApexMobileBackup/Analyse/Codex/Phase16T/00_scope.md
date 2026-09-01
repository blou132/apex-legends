# Phase16T scope

Phase16T was PC-only, static, and read-only. It started from the exact
Phase16S Shutdown cell and inspected only ELF `0x0ae56600..0x0ae56b00`, the
functions referenced by the recovered local groups, and exact references to
the two resulting address points.

No phone, ADB, Apex process, ptrace, breakpoint, AppData, GameProtector,
network, binary modification, or writable Ghidra operation was used.
