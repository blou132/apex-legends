# Phase16I scope

Phase16I used only the rooted sacrificial PRA-LX1 and the exact preserved Apex
Mobile package. The device stayed offline for installation, warm-up, tracing,
and cleanup. No Samsung or emulator was connected.

The active scope was one ARM64 execution breakpoint at
`CVersionMgrImp::Init`. The tracer performed no software breakpoint, process
memory write, code patch, injection, APK/OBB change, or broad memory scan.
Runtime addresses, raw AppData, logs, process identifiers, and compiled
binaries remain in gitignored `LocalInputs` storage.

The single active breakpoint attempt timed out. It was explicitly disabled,
the traced thread was detached, and Apex was force-stopped. No retry occurred.
