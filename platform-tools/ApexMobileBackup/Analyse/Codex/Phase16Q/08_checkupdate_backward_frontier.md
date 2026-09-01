# CheckUpdate backward frontier

The earliest reliable continuation begins at ELF `0x05a2f000` / Ghidra
`0x05b2f000`. A direct-edge CFG over only the coherent continuation contains
206 basic blocks; 106 are reverse-reachable from the block containing the
`+0x1f0` load at ELF `0x05a2f094`.

The reverse walk stops at `0x05a2f000`. It has no predecessor inside the
reliable CFG, and preceding words belong to the mixed opaque prefix.

```text
CHECKUPDATE_FIRST_READABLE_ELF = 0x05a2f000
CHECKUPDATE_FIRST_READABLE_GHIDRA = 0x05b2f000
CHECKUPDATE_BACKWARD_FRONTIER = ELF_0x05a2f000
CHECKUPDATE_FRONTIER_REASON = OPAQUE_OR_DATA_LIKE_BYTES_BEFORE_READABLE_CONTINUATION
```
