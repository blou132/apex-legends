# Evidence table

| Claim | Evidence | Result |
| --- | --- | --- |
| Inputs are exact | independent PC and device hashes; APK metadata/signature | confirmed |
| Execution remained offline | airplane mode, disabled radios, empty routes, no default network at all gates | confirmed by isolation state |
| Static target is exact | ELF/Ghidra model plus bounded entry disassembly | confirmed |
| Callback argument is defined | entry dataflow stores `*X1` at manager `+0x18` | confirmed |
| Slot semantics are defined | strategy callback, vptr, slot `+0x28` dispatch | confirmed |
| One thread strategy exists | historical caller role plus current `Thread-10` GCloud evidence | medium confidence |
| Runtime target is exact | fresh offset-zero map plus 16-byte memory/local match | confirmed |
| Hardware programming worked | active SET success and Huawei normalized readback | confirmed |
| Target executed | 90-second wait produced no SIGTRAP | not observed |
| Cleanup completed | explicit disable, detach, force-stop, no residual process | confirmed |
| Callback target resolved | no entry trap/register capture | no |
