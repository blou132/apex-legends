# Registration function

`FUN_005bc4bc` at Ghidra `0x005bc4bc`, ELF `0x004bc4bc`, is the exact
registration API and performs the only proven non-null assignment. It occupies
`cu::CActionMgr` vtable slot `+0xe0` at Ghidra `0x0097b0e0`.

`FUN_0050c4a8` at Ghidra `0x0050c4a8`, ELF `0x0040c4a8`, is the higher-level
supplier method. It stores its callback argument at
`CVersionStrategy+0x18`, loads the strategy's `CActionMgr` from `+0x20`, and
dispatches manager slot `+0xe0` at Ghidra `0x0050c4ec`/`0x0050c4f4`, passing
the strategy object itself.

The stripped code provides no exact source-level method name for the
registration API. Its proven semantic is therefore recorded conservatively as
a non-null callback setter, not as an invented `RegisterCallback` symbol.

There are no direct call xrefs to either virtual method. The resolved caller
levels are:

1. `FUN_0050c4a8` invokes `FUN_005bc4bc` through `CActionMgr` slot `+0xe0`.
2. `FUN_00576890` (`CVersionMgrImp::CheckAppUpdate`) invokes
   `FUN_0050c4a8` through strategy slot `+0x48` at Ghidra
   `0x005769a0`/`0x005769a8`.

The thin wrapper `FUN_0050691c` is recorded as the direct caller of
`FUN_00576890`; analysis stops there, within the two-level limit.
