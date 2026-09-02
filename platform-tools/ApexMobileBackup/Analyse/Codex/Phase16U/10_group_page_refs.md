# Group page references

Eight AArch64 `ADRP` instructions target ELF page `0x0ae56000`. Read-only
Ghidra disassembly confirms that their bounded arithmetic reaches only:

`0x0ae560b8`, `0x0ae569e8`, `0x0ae56c90`, `0x0ae56ca8`,
`0x0ae56f68`, and `0x0ae56fd0`.

None enters the authorized DolphinUpdater group
`0x0ae565f8..0x0ae56908`.

`GROUP_PAGE_ADRP_COUNT = 8`
