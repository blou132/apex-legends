# Preflight

- Starting branch: `main`
- Starting state: clean and synchronized with `origin/main`
- Starting HEAD: `e84d08cb1ee23743ad2873b4c900bb3d91feafce`
- Input SHA-256: `0d45f92c5d0d2805da35f223b8e3d352041943c9c2b2b5c914f0e1a1c1ec2977`
- Input format: ELF64 little-endian AArch64
- Ghidra project: existing `ApexMobileUE4`, opened read-only with analysis
  disabled

`PHASE16Y_PREFLIGHT = PASS`

`LIBUE4_EXACT_INPUT_VALID = YES`

`GHIDRA_READ_ONLY = YES`

Finalization on 2026-09-05 rechecked the same ELF SHA-256 and header
(ELF64, little-endian, AArch64). The only pending Git files were the new
Phase16Y reports and cleaned JSON. The ELF and raw helper exports remain
ignored.
