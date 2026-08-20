# CPU and virtualization

| Property | Result |
| --- | --- |
| CPU vendor | AMD |
| CPU model | AMD Ryzen 7 3700U with Radeon Vega Mobile Gfx |
| Firmware virtualization | `CONFIRMED ENABLED` |
| SLAT | `CONFIRMED SUPPORTED` |
| VM monitor mode extensions | `CONFIRMED SUPPORTED` |
| Windows hypervisor currently present | `CONFIRMED NO` |

The CPU and firmware meet the fundamental Android Emulator acceleration
requirements. No BIOS change is indicated.

`bcdedit /enum` requires an elevated session on this host and the read-only
query was denied. Therefore the configured `hypervisorlaunchtype` is preserved
as `UNKNOWN_ACCESS_DENIED`; it is not inferred from the absence of a currently
running hypervisor.

```text
HYPERVISOR_LAUNCH_TYPE = UNKNOWN_ACCESS_DENIED
```
