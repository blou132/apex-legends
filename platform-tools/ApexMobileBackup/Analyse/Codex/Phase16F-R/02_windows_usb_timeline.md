# Windows USB timeline

All times use the host's Europe/Paris clock. Exact times come from Windows or
the host transcript. Inferred and order-only rows are labelled explicitly.
Device-instance suffixes, COM numbers, and container identifiers are omitted.

| Point | Time | Evidence | Event |
| --- | --- | --- | --- |
| T0 | Between 15:16:18 and 15:18:51 | Operator instruction plus T1 | Known physical sequence began; exact contact instant was not logged. |
| T1 | 15:18:51.431 | SetupAPI | `USB\VID_12D1&PID_3609` hardware-initiated install began. |
| T1a | 15:18:51.730-15:18:51.877 | System and SetupAPI | `HSPL_usbvcom` was installed, the device started, and configuration exited successfully. |
| T1b | 15:18:51.953-15:18:52.032 | DeviceSetupManager | `HUAWEI USB COM 1.0` property processing completed. |
| T2 | 15:29:48.963 | Host transcript | PotatoNV Start was invoked. |
| T3 | After T2, exact time absent | PotatoNV ordered log | `xloader` upload occurred. |
| T4 | After T3, exact time absent | PotatoNV ordered log | RAM `fastboot` upload occurred. |
| T5 | After T4 and before T6 | State transition | The bootrom endpoint ceased to be the active PotatoNV transport; no exact removal event was preserved. |
| T6 | About 15:30:04.079, inferred lower bound | DeviceSetupManager duration | Processing of the new D00D container began 21.406 s before completion. |
| T7 | 15:30:25.418-15:30:25.485 | DeviceSetupManager | `USB\VID_18D1&PID_D00D` had no matching non-optional driver; `Fastboot2.0` properties completed. |
| T7a | Later bounded inspection | Phase16F PnP snapshot | `Fastboot2.0` had Configuration Manager problem code 28. |
| T8 | After T7; no later than 15:32:00.649 capture | PotatoNV log and source | The approximately 25 s libusb wait ended with `Timeout error`. Exact emission time was not preserved. |
| T9 | 15:44:07.907-15:44:09.048 | SetupAPI | `hw_goadb.inf` was imported as `oem77.inf` and configured for D00D using WinUSB. |
| T10 | 2026-08-29 around 12:41-12:48 | SetupAPI plus operator transcript | Later physical retries enumerated normal Huawei USB (`VID_12D1/PID_107E`), not bootrom. |

## Event-log coverage

- `Microsoft-Windows-DeviceSetupManager/Admin`: enabled and useful.
- `Microsoft-Windows-DeviceSetupManager/Operational`: enabled, but no relevant
  event in the bounded window.
- `Microsoft-Windows-DriverFrameworks-UserMode/Operational`: disabled, so no
  historical evidence was available there.
- `System`: useful service-install and UserPnp success records for VCOM.
- No useful bounded Kernel-PnP message added a removal timestamp.

```text
FIRST_BOOTROM_TIMELINE = PHYSICAL_SEQUENCE -> 12D1:3609_BOUND -> POTATONV_START -> XLOADER -> RAM_FASTBOOT -> 18D1:D00D_UNBOUND -> PROBLEM_28 -> TIMEOUT -> DRIVER_INSTALLED_LATER
TIMELINE_PRECISION = MIXED_EXACT_INFERRED_AND_ORDER_ONLY
```
