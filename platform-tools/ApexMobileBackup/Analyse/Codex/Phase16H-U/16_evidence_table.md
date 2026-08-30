# Evidence table

| Claim | Evidence | Classification |
|---|---|---|
| Preflight safe | one PRA-LX1, root, enforcing, Apex absent | confirmed |
| Tracee build valid | ELF/readelf/nm/disassembly and matching hashes | confirmed |
| Runtime address valid | PT_LOAD plus RX maps correlation | confirmed |
| Attach and GPR regset | attach stop and 272-byte readback | confirmed |
| Hardware regset | 264 bytes, six clear execution slots | confirmed |
| Disabled address SET | syscall success | confirmed |
| Address retained | immediate readback mismatch | disproved for tested sequence |
| Active control programmed | checkpoint not reached | not tested |
| Function-entry trap | no continue | not tested |
| x0-x7 capture | no trap | not tested |
| Cleanup | safety kill, no process/artifact, healthy device | confirmed |

The most specific supported blocker is the disabled address-only pre-step. The
exact returned address was not logged, so no stronger kernel explanation is
claimed.
