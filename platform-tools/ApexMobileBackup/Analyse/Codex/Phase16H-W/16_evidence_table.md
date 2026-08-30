# Evidence table

| Claim | Classification | Evidence |
| --- | --- | --- |
| Sources and binaries matched Phase16H-V | Confirmed | Clean Git state, build, symbols, matching hashes |
| Runtime target belonged to current tracee RX mapping | Confirmed | Fresh ELF/PT_LOAD/maps correlation |
| Active slot-0 SET succeeded | Confirmed | Tracer runtime output |
| Address matched and cached control was `0x41e4` | Confirmed | Immediate GETREGSET output |
| Stop was an ARM64 hardware breakpoint | Confirmed | SIGTRAP, `si_code=4`, matching trap address |
| Trap occurred at exact function entry | Confirmed | PC equality |
| X0-X7 contained the expected arguments | Confirmed | Eight runtime comparisons |
| Explicit disable succeeded | Confirmed | SETREGSET return |
| Diagnostic disabled readback remained cached | Confirmed | Local raw GETREGSET output |
| Breakpoint did not retrigger | Confirmed | Correct second call and controlled SIGSTOP |
| Tracee remained correct after detach | Confirmed | Post-detach result and bounded health output |
| Capability transfers unchanged to Apex | Not yet tested | Requires separately authorized Phase16I |

No runtime address, map, device identifier, or raw register dump is committed.
