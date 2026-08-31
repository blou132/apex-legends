# Computed dispatch site

The Phase16K site at Ghidra `0x0a4c18fc` is ELF `0x0a3c18fc`. Raw bytes decode
to `br x17`, the fourth instruction of the `CreateDolphin` PLT stub. `x17` is
loaded from GOT cell `0x0b37e8c8`, whose relocation is the exact imported
factory symbol.

It is not a function boundary, veneer, or independent computed caller.

```text
COMPUTED_CALL_SITE_CLASS = FINAL_BR_IN_STANDARD_AARCH64_PLT_STUB
COMPUTED_CALL_REAL_TARGET_SYMBOL = CreateDolphin
```
