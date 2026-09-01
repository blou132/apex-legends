# Preflight

The starting branch was clean and synchronized with `origin/main` at
`c78b037dc4e324ad9297edf909da3a76b18783be`.

| Input | SHA-256 | Format |
|---|---|---|
| `libUE4.so` | `0D45F92C5D0D2805DA35F223B8E3D352041943C9C2B2B5C914F0E1A1C1EC2977` | ELF64 LE AArch64 |
| `libgcloud.so` | `2EDF04E3A50BEEAE3E653BE99ECAFD0AB916AF665B72B653742D4B7CBF7D1C7E` | ELF64 LE AArch64 |

The existing `ApexMobileUE4` Ghidra project was opened with `-readOnly` and
`-noanalysis`. No import or full autoanalysis was performed.

```text
PHASE16Q_PREFLIGHT = PASS
LIBUE4_EXACT_INPUT_VALID = YES
LIBGCLOUD_EXACT_INPUT_VALID = YES
GHIDRA_EXISTING_PROJECT_REUSED = YES
GHIDRA_READ_ONLY = YES
```
