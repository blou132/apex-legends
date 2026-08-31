# PLT index derivation

The first symbol stub starts at ELF `0x0a3bf7a0`, after the 32-byte PLT0. Four
unrelocated 16-byte stubs occupy `0x0a3bf8e0` through `0x0a3bf910`, after
relocation index 19. For relocation index 529, the physical stub index is
therefore `529 + 4 = 533`.

```text
0x0a3bf7a0 + (533 * 0x10) = 0x0a3c18f0
```

The Ghidra project uses the independently validated `+0x100000` bias.

```text
CREATEDOLPHIN_EXPECTED_PLT_ELF = 0x0a3c18f0
CREATEDOLPHIN_EXPECTED_PLT_GHIDRA = 0x0a4c18f0
PLT_ENTRY_SIZE = 16
PLT_RESERVED_ENTRY_COUNT = 1 32-byte PLT0; four additional unrelocated dummy stubs
```
