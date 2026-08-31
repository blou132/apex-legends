# Evidence table

| Claim | Raw site | Manual target / mapping | Classification |
|---|---|---|---|
| Exact ELF inputs | Preserved hashes and ELF headers | ELF64 AArch64 | CONFIRMED |
| Address model | PT_LOAD, call bytes, GOT and project image base | Ghidra = ELF + `0x100000` | CONFIRMED |
| Factory relocation | `.rela.plt` entry 529 | GOT `0x0b37e8c8`, dynsym 375 | CONFIRMED |
| Factory PLT stub | ELF `0x0a3c18f0` | decodes GOT `0x0b37e8c8` | CONFIRMED |
| Neighbor mapping | indices 528, 530, 531 | each stub decodes its exact GOT cell | CONFIRMED |
| Direct call | ELF `0x05a314cc`, opcode `0x95264109` | target `0x0a3c18f0` | CONFIRMED |
| Ghidra PLT shift | Same branch/GOT/relocation chain | no target mismatch | INVALIDATED |
| Loader changes call target | `.rela.dyn` target window | zero entries | INVALIDATED |
| Function boundary | FDE `0x05a311a8...0x05a31ca8` | matches Ghidra after bias | CONFIRMED |
| Thunk or veneer | exact-target AArch64 branch scan | one direct BL, no B | INVALIDATED |
| Returned object retained | `x0 -> x1 -> throw_length_error` | no store or passthrough | INVALIDATED |
| Client Init dispatch | Proven returned-pointer path | pointer lost first | UNKNOWN |
| Deterministic trace trigger | Proven static path | no owner/pre-Init gate | UNKNOWN |
