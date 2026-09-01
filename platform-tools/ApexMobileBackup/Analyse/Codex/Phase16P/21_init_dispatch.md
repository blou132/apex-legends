# Real Dolphin Init dispatch

In the readable continuation of `DolphinUpdater::CheckUpdate`, Ghidra
`0x05b2f094` loads the persistent interface from `owner+0x1f0`. It loads the
function at vtable slot `+0x10` and calls it at Ghidra `0x05b2f0ac` (ELF
`0x05a2f0ac`). This is the known `GCloudDolphinImp::Init` slot.

The client-side interface dispatch is therefore proven. The concrete dynamic
vptr target remains probable rather than confirmed because acquisition
provenance is hidden.

```text
REAL_DOLPHIN_INIT_DISPATCH_PROVEN = YES
DOLPHIN_INIT_CALLER = DolphinUpdater::CheckUpdate / protected FUN_05b2eb00 region
DOLPHIN_INIT_CALL_SITE = GHIDRA_0x05b2f0ac / ELF_0x05a2f0ac
DOLPHIN_INIT_TARGET = GCloudDolphinImp::Init / FUN_005458a0 (PROBABLE_DYNAMIC_TARGET)
```
