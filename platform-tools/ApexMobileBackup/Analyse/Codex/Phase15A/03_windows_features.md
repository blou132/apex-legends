# Windows features and VBS

`Win32_OptionalFeature` provides a non-elevated, read-only inventory. Its
install-state value `2` identifies both relevant listed features as disabled.

| Feature | Availability | State |
| --- | --- | --- |
| Windows Hypervisor Platform (`HypervisorPlatform`) | `CONFIRMED YES` | `DISABLED` |
| Hyper-V role (`Microsoft-Hyper-V-All`) | `NOT_LISTED_ON_THIS_EDITION` | `NOT_AVAILABLE_IN_INVENTORY` |
| Virtual Machine Platform (`VirtualMachinePlatform`) | `CONFIRMED YES` | `DISABLED` |

Windows Hypervisor Platform and the full Hyper-V role are not the same feature.
WHPX is present on this installation even though the full role is not listed.

The Device Guard status provider reports VBS status `0`, no configured security
service, and no running security service. The matching VBS and HVCI registry
settings are absent.

```text
VBS_STATE = DISABLED
MEMORY_INTEGRITY_STATE = DISABLED
```
