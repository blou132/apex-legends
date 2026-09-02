# Target relocations

All ELF relocation records were tested only for resolved targets in
`0x0ae565f8..0x0ae56908`. No relocation targets the primary address point,
secondary address point, either header, or another internal group address.

The result was independently reproduced by reading Ghidra's applied value at
each relocation source.

`GROUP_TARGET_RELOCATION_COUNT = 0`
