# Evidence table

| Claim | Classification | Basis |
| --- | --- | --- |
| H-U submitted address plus disabled control in 24 bytes | Confirmed | Exact H-U source and layout |
| H-U address readback mismatched | Confirmed | H-U cleaned runtime output |
| Exact H-U returned address | Unknown | Value was not logged |
| H-S submitted address plus active control | Confirmed | H-S source and runtime output |
| H-S address matched and control returned `0x41e4` | Confirmed | H-S runtime output |
| Kernel applies address before control | Confirmed | PRA source and exact C33 disassembly |
| Disabled control skips validation | Confirmed | Kernel source control flow |
| Disabled path caused H-U non-retention | Probable | Strong source/binary correlation; missing exact H-U return |
| Active control triggers full validation | Confirmed | Kernel source and H-S behavior |
| `0x41e4` is expected Huawei cached normalization | Confirmed | Phase16H-T source/binary reconstruction |
| New iovec writes only slot 0 | Confirmed | Static offsets, assertions, disassembly |
| Future function-entry trap will occur | Unknown until runtime | Phase16H-V did not execute the tracer |

No inference is presented as runtime proof.
