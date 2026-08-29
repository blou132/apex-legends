# Bootrom live binding

The live `HUAWEI USB COM 1.0` endpoint reported status `OK` and problem code
zero. Windows bound the previously validated signed package:

| Field | Value |
| --- | --- |
| VID/PID | `12D1:3609` |
| INF | `oem14.inf` / `hw_usbvcom.inf` |
| Provider | Huawei Incorporated |
| Version | `2.0.7.1` |
| Service | `HSPL_usbvcom` |
| Result | Live binding success |

```text
BOOTROM_LIVE_DRIVER_BOUND = YES
BOOTROM_PROBLEM_CODE = 0
```
