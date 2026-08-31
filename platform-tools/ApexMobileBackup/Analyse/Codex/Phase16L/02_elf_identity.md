# Exact ELF identity

| Input | Size | SHA-256 | Class / machine |
|---|---:|---|---|
| `libUE4.so` | 190192944 | `0D45F92C5D0D2805DA35F223B8E3D352041943C9C2B2B5C914F0E1A1C1EC2977` | ELF64 / AArch64 |
| `libgcloud.so` | 9041528 | `2EDF04E3A50BEEAE3E653BE99ECAFD0AB916AF665B72B653742D4B7CBF7D1C7E` | ELF64 / AArch64 |

The hashes and sizes match the exact preserved inputs already established in
Phase16K and the existing Ghidra project metadata.

The executable `libUE4.so` PT_LOAD maps file offset `0x039593e0` to ELF VA
`0x0395a3e0`. The callsite bytes at ELF `0x05a314cc` are therefore read from
file offset `0x05a304cc`. The same bytes are represented in Ghidra at
`0x05b314cc`. The PLT and GOT addresses independently preserve the same bias.

```text
LIBUE4_EXACT_INPUT_VALID = YES
LIBGCLOUD_EXACT_INPUT_VALID = YES
LIBUE4_ADDRESS_MODEL_VALIDATED = YES
LIBUE4_GHIDRA_BIAS = +0x100000
```
