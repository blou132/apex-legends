# Device and firmware identity

## Sanitized identity

| Field | Result | Classification |
| --- | --- | --- |
| Model | `PRA-LX1` | CONFIRMED by ADB property |
| Device | `HWPRA-H` | CONFIRMED by ADB property |
| Board/platform/hardware | `hi6250` | CONFIRMED by three ADB properties |
| SoC | HiSilicon Kirin 655 / hi6250 family | CONFIRMED by hardware properties plus Prague device documentation |
| Android | `8.0.0` | CONFIRMED |
| EMUI | `EmotionUI_8.0.0` | CONFIRMED |
| Full build | `PRA-LX1 8.0.0.364(C33)` | CONFIRMED |
| Incremental | `364(C33)` | CONFIRMED |
| CUST/region | `C33`, Altice `all` | CONFIRMED by build and `/cust/altice/all` |
| Security patch | `2019-03-01` | CONFIRMED |
| Build fingerprint | `HUAWEI/PRA-LX1/HWPRA-H:8.0.0/HUAWEIPRA-LX1/364(C33):user/release-keys` | CONFIRMED; non-unique build metadata |

The SoC conclusion is not inferred from the retail model alone. The running
device reports `hi6250` independently as board, board platform, and hardware.
The public Prague device tree identifies the PRA-LX1 platform as Kirin 655:
[Prague device tree](https://github.com/friedensfurz/android_device_huawei_prague).

## Current lab state

- Apex version: `1.3.672.546` (`64003140`).
- Apex package state during and after the audit: stopped.
- Battery at audit: `96%`.
- `/data` available: `2123708 KiB` (about `2.03 GiB`).
- USB debugging and the current ADB authorization are working.
- Host `adb.exe` and `fastboot.exe` are present.
- Common audited locations did not contain PotatoNV, Magisk, HiSuite, or the
  exact stock firmware package/images.
- Huawei USB COM testpoint drivers were not validated.

No private Apex data was accessed.
