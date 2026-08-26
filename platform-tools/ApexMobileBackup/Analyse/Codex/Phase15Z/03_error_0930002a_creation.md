# Error 0x0930002a creation

The exact code is created inside `FUN_00550ee4`. The AArch64 instruction pair
loads low reason `0x2a` and high prefix `0x0930`, then calls the error relay
`FUN_00549800` at Ghidra `0x005516f4`.

No standalone little-endian 32-bit or 64-bit `0x0930002a` data value exists in
the binary, and no single instruction exposes the complete scalar. Ghidra's
decompiler reconstructs the split immediate as the literal `0x930002a`.

Other connected branches combine low-byte reasons with `0x09300000`, proving a
Dolphin error-family prefix. The semantic meaning of high byte `0x09` and
middle byte `0x30` is not named by the connected code.

DOLPHIN_0930002A_ANCHORS = FUN_00550ee4 Ghidra 0x005516e8-0x005516f4
DOLPHIN_ERROR_CREATOR = FUN_00550ee4 -> FUN_00549800
DOLPHIN_ERROR_CREATION_STYLE = BITFIELD
