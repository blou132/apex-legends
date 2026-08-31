# Phase16L scope

Phase16L is a PC-only, static, read-only reconstruction of the exact
`libUE4.so` import path for `CreateDolphin`. The analysis is limited to the
target relocation, its PLT/GOT neighborhood, exact machine branches to that
stub, bounded unwind records, and the returned pointer flow.

No phone, ADB, Apex launch, AppData access, runtime instrumentation, network
operation, breakpoint, memory write, or binary modification was used. Raw
tool output was not committed.
