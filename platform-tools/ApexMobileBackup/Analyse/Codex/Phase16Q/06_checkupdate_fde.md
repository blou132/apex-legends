# CheckUpdate FDE

`.eh_frame_hdr` selects the FDE at `0x0337ef90`; its `zR` CIE is at
`0x02c58db0`. Decoding the FDE gives the exact ELF range
`0x05a2eb00..0x05a2fdbc` (end exclusive), 4796 bytes. The Ghidra image uses a
`+0x00100000` mapping, yielding `0x05b2eb00..0x05b2fdbc`.

The real Init dispatch at ELF `0x05a2f0ac` lies inside this range.

```text
CHECKUPDATE_FDE_START_ELF = 0x05a2eb00
CHECKUPDATE_FDE_END_ELF = 0x05a2fdbc_EXCLUSIVE
CHECKUPDATE_FUNCTION_RANGE_VALID = YES
```
