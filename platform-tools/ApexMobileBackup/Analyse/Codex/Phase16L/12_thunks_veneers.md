# Thunks and veneers

A compiled exact-target scan decoded every aligned AArch64 `BL` and `B`
instruction in `.text` and retained only branches whose calculated target is
the proven stub `0x0a3c18f0`. It found one direct `BL` and no `B` veneer.

The target itself is a normal four-instruction PLT stub. No intermediate
branch island or tail-call thunk participates in this edge.

```text
CALLSITE_THUNK_PRESENT = NO
CALLSITE_VENEER_PRESENT = NO
FINAL_IMPORT_TARGET_AFTER_THUNKS = CreateDolphin
```
