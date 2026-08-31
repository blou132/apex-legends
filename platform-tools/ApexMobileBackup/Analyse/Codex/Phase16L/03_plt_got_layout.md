# PLT, GOT, and relocation layout

| Section | ELF VA | File offset | Size | Entry size | Alignment |
|---|---:|---:|---:|---:|---:|
| `.plt` | `0x0a3bf780` | `0x0a3be780` | `0x2ca0` | 16-byte symbol stubs; 32-byte PLT0 | 16 |
| `.got` | `0x0b3776e8` | `0x0b3756e8` | `0x6120` | 8-byte cells | 8 |
| `.got.plt` | `0x0b37d808` | `0x0b37b808` | `0x1658` | 8-byte cells | 8 |
| `.rela.plt` | `0x318` | `0x318` | `0x4308` | 24 | 8 |
| `.rela.dyn` | `0x72d0` | `0x72d0` | `0x20d5388` | 24 | 8 |
| `.dynsym` | `0x0b8cc5b8` | `0x0b55c5b8` | `0x4e48` | 24 | 8 |
| `.dynamic` | `0x0b8d1400` | `0x0b561400` | `0x2f0` | 16 | 8 |

There is no `.plt.sec`. `.rela.plt` contains 715 entries. Entries 0 through
707 map the main `.plt` relocation-backed stubs. Four unrelocated dummy stubs
are inserted after relocation index 19. The final seven relocations target a
separate late image area and are irrelevant to the target index 529.

```text
PLT_SECTION_MODEL = 32-byte PLT0; 16-byte AArch64 stubs; four unrelocated dummy stubs after relocation 19
GOT_SECTION_MODEL = three reserved GOT.PLT cells followed by relocation and dummy cells
RELA_PLT_ENTRY_SIZE = 24
RELA_PLT_COUNT = 715
```
