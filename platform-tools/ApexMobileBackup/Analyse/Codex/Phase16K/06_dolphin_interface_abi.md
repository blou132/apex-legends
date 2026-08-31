# Dolphin interface ABI

The minimum interface ABI needed for this phase remains confirmed from the
exact `libgcloud.so` project:

| Item | Ghidra address / offset | Target |
|---|---|---|
| Primary vtable | `0x00979620` | `GCloudDolphinImp` |
| Init slot | `+0x10`, cell `0x00979630` | `FUN_005458a0` |
| Secondary vtable | `0x009796d8` | secondary interface portion |

`FUN_005458a0` is the previously resolved `GCloudDolphinImp::Init`. No broader
class reconstruction was required.

```text
DOLPHIN_INTERFACE_VTABLE = 0x00979620
DOLPHIN_INIT_SLOT_OFFSET = +0x10
DOLPHIN_INIT_TARGET = FUN_005458a0
```
