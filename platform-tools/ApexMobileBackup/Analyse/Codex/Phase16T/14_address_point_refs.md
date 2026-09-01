# Address-point references

After recovering the two exact address points, two independent checks were
performed:

- a structured ELF64 AArch64 decoder scanned `.text` for exact `ADR` and
  `ADRP+ADD` constructions;
- Ghidra's reference manager queried both exact address points for code and
  data references.

Both returned zero. No broader table-address or constructor scan followed.

`ADDRESS_POINT_CODE_REFERENCE_COUNT = 0`
