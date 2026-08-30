# Evidence table

| Claim | Status | Evidence |
| --- | --- | --- |
| Runtime target address was valid | CONFIRMED | PT_LOAD, maps, symbol, alignment, local instruction bytes |
| Generic root attach/getregs/detach works | CONFIRMED | Software probe and Phase16H-R |
| Software breakpoint can be inserted | INVALIDATED | One POKETEXT returned EIO before any write |
| ARM64 HW breakpoint regset exists | CONFIRMED | GETREGSET length 264, six execution slots |
| Hardware breakpoint was safely armed | NOT PROVEN | Control readback differed from requested value |
| Hardware state was restored in-process | NOT PROVEN | Restore validation failed; tracees were killed |
| Function-entry SIGTRAP occurred | NO EVIDENCE | Neither branch reached continue-to-target |
| X0-X7 arguments were captured | NO EVIDENCE | No function-entry stop occurred |
| Phase16I is ready | INVALIDATED | No breakpoint method passed end-to-end validation |
| Device security baseline survived | CONFIRMED | Root healthy, SELinux enforcing, no persistent files/processes |
