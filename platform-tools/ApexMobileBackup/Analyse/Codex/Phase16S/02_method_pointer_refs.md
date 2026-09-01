# Exact method pointer references

Only `.data.rel.ro`, `.rodata`, and `.data` were scanned for exact 64-bit
values. Code immediates and generic indirect calls were excluded.

| Method | Ghidra address | ELF address | Raw exact pointer hits |
| --- | --- | --- | ---: |
| OnNoticeInstallApk | `0x080f68e8` | `0x07ff68e8` | 0 |
| first-extract owner continuation | `0x080f6f04` | `0x07ff6f04` | 0 |
| Shutdown | `0x04812c6c` | `0x04712c6c` | 1 |
| CheckUpdate region entry | `0x05b2eb00` | `0x05a2eb00` | 0 |

The one hit is the `.data.rel.ro` cell at Ghidra `0x0af56888` (ELF
`0x0ae56888`) containing `0x04812c6c`. Ghidra's reference manager reported no
data references for the four methods, so the output counts use the raw exact
pointer pass.

These are bounded negative results for the three scanned sections, not claims
that no encoded or dynamically produced reference exists elsewhere.
