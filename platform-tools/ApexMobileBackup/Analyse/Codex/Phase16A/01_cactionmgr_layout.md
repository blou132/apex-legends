# CActionMgr callback layout

RTTI and constructor dataflow retain the Phase15Z identification of
`cu::CActionMgr`:

- vtable: Ghidra `0x0097b000`, ELF `0x0087b000`
- constructor: `FUN_005bfb10`, Ghidra `0x005bfb10`, ELF `0x004bfb10`
- object size allocated by `FUN_00579000`: `0x3c8`
- external result callback field: `+0x3b8`

The constructor writes zero to this field at Ghidra `0x005bfdd0`. The
destructor `FUN_005bff30` writes zero again at `0x005bff78`.
`FUN_005be71c` (`ProcessActionError`) reads the field and invokes its slot
`+0x00` with the action stage and queued raw error code.

For the established witness, those values are stage `69` (`0x45`) and raw
error `0x0930002a`.
