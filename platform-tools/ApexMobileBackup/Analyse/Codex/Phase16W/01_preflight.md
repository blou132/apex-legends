# Preflight

The starting branch was clean and synchronized:

```text
BRANCH = main
STARTING_HEAD = 3332ded Resolve DolphinUpdater method-caller provenance Phase16V
MAIN_EQUALS_ORIGIN_MAIN = YES
PHASE16W_PREFLIGHT = PASS
```

Exact inputs:

| Input | Size | SHA-256 | Format |
|---|---:|---|---|
| `libUE4.so` | 190192944 | `0D45F92C5D0D2805DA35F223B8E3D352041943C9C2B2B5C914F0E1A1C1EC2977` | ELF64 AArch64 LE |
| `libgcloud.so` | 9041528 | `2EDF04E3A50BEEAE3E653BE99ECAFD0AB916AF665B72B653742D4B7CBF7D1C7E` | ELF64 AArch64 LE |

Both existing Ghidra projects were opened with `-noanalysis -readOnly`.

```text
LIBUE4_EXACT_INPUT_VALID = YES
LIBGCLOUD_EXACT_INPUT_VALID = YES
GHIDRA_READ_ONLY = YES
```
