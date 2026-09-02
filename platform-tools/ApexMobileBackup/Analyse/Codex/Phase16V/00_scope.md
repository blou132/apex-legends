# Phase16V scope

Phase16V is PC-only, static, and read-only. It follows exact callers of four
already-proven DolphinUpdater method anchors to recover caller-side `X0` / this
provenance. It does not reopen the exhausted address-point, VTT, constructor,
or global `+0x1f0` axes.

Allowed work was limited to exact AArch64 `B`/`BL` targets, the exact
CheckUpdate FDE, one thunk level, bounded caller provenance, and one exact
field writer axis after storage convergence was proven.

No phone, ADB, runtime launch, ptrace, breakpoint, network, AppData, or binary
modification was used.
