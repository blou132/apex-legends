# Preflight

The repository started clean on `main`, synchronized with `origin/main`, at
commit `95d717d01e2cf45b726cce895eb928111cc9ce80`.

Three preserved copies of `libgcloud.so` were compared. They have identical
size and SHA-256:

```text
size = 9041528 bytes
sha256 = 2EDF04E3A50BEEAE3E653BE99ECAFD0AB916AF665B72B653742D4B7CBF7D1C7E
```

ELF metadata identifies the input as ELF64, AArch64, shared object. The exact
`libUE4.so` matches the executable hashes recorded by the existing Ghidra
project. The historical address relationship was revalidated:

```text
Ghidra address = ELF virtual address + 0x100000
```

```text
PHASE16K_PREFLIGHT = PASS
LIBGCLOUD_EXACT_INPUT_VALID = YES
GHIDRA_EXISTING_PROJECT_REUSED = YES
GHIDRA_READ_ONLY = YES
```
