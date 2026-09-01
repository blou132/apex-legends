# Shutdown cell

ELF cell `0x0ae56888` is in `.data.rel.ro`. Its raw file bytes are zero because
the ELF uses RELA. The matching `.rela.dyn` record is `R_AARCH64_RELATIVE`
(type 1027), symbol index zero, addend `0x04712c6c`. Ghidra applies the same
relocation as `0x04812c6c`, the known `DolphinUpdater::Shutdown` function.

`SHUTDOWN_TABLE_CELL_VALID = YES`

`SHUTDOWN_TABLE_CELL_RELOCATION = R_AARCH64_RELATIVE; ADDEND ELF_0x04712c6c`
