# Phase16Y scope

Phase16Y was authorized as a PC-only, static, read-only audit of the exact
preserved libUE4.so, limited to GameUpdateMgr and DolphinUpdater reflection
metadata.

The accepted results concern the two exact class accessors, their
registration arguments, and directly connected metadata. The local helpers
over-collected shared package references and invoked the decompiler on opaque
callbacks. This execution deviation is recorded in [30_boundary.md](30_boundary.md);
strict class-only execution is not claimed.

No phone, ADB, Apex process, runtime memory, ptrace, breakpoint, or binary
modification was used. Ghidra opened the program read-only with analysis
disabled. Application data was not inspected; Ghidra performed its normal
host preference/log I/O. Analysis used no network; the requested Git push is
a separate publication operation.

Raw exports and helpers remain local-only and gitignored. This directory
contains sanitized metadata and conclusions only.
