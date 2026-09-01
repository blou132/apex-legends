# Local relocation run

The authorized window was exactly ELF `0x0ae56600..0x0ae56b00` (Ghidra
`0x0af56600..0x0af56b00`). It contains 160 aligned 8-byte cells and 145 local
relocations. The full cell map and relocation dump are retained local-only.

The decisive layout is a contiguous executable-pointer run from
`0x0ae56608` through `0x0ae56898`, followed by `-0x28`, zero, and a second
executable-pointer run beginning at `0x0ae568b0`.
