# SetupAPI and driver analysis

## Bootrom endpoint

The VCOM package was staged at 14:48:32, before a matching device existed.
When `VID_12D1/PID_3609` appeared at 15:18:51, SetupAPI selected the exact
configuration and completed a live device start successfully.

| Field | Value |
| --- | --- |
| Published INF | `oem14.inf` |
| Original INF | `hw_usbvcom.inf` |
| Exact coverage | `USB\VID_12D1&PID_3609` |
| Driver version | `2.0.7.1` |
| Service/binary | `HSPL_usbvcom` / `hw_usbvcom.sys` |
| SetupAPI result | Success, device started |
| Current catalog state | Valid Microsoft Windows Hardware Compatibility Publisher signature |

## Temporary fastboot endpoint

At first appearance, DeviceSetupManager explicitly reported no matching
non-optional driver for `VID_18D1/PID_D00D`. The later Phase16F PnP snapshot
recorded problem code 28. PotatoNV's libusb search therefore could not open
the endpoint even though Windows had enumerated it physically.

The matching package was installed only after the timeout:

| Field | Value |
| --- | --- |
| Published INF | `oem77.inf` |
| Original INF | `hw_goadb.inf` |
| Exact coverage | `USB\VID_18D1&PID_D00D` |
| Driver version | `2.0.5.0` |
| INF description | `Android Sooner Single ADB Interface` |
| Service/binary | `WinUSB` / `WinUSB.sys` |
| First live result | No matching driver; problem code 28 |
| Post-timeout SetupAPI result | Package/device-node configuration success |
| Current catalog state | Valid Microsoft Windows Hardware Compatibility Publisher signature |

The current driver store still contains both exact packages, both required
system binaries are present, and `pnputil /enum-drivers` identifies both
published names. The post-timeout D00D install configured the existing device
node after the transient endpoint was gone. A live D00D re-enumeration has not
occurred since installation, because this phase did not connect the phone.

The absence of a D00D WinUSB binding is sufficient to explain this observed
PotatoNV timeout: the expected endpoint existed, the source waited for that
exact VID/PID through libusb, Windows reported no driver and problem 28, and
the log stopped at that wait. This does not prove that no later latent problem
would occur in a future run.

```text
BOOTROM_INF = oem14.inf
BOOTROM_DRIVER = hw_usbvcom.sys / HSPL_usbvcom
BOOTROM_DRIVER_BIND_RESULT = SUCCESS

TEMP_FASTBOOT_INF = oem77.inf
TEMP_FASTBOOT_DRIVER = WinUSB.sys via hw_goadb.inf
TEMP_FASTBOOT_FIRST_BIND_RESULT = NO_MATCHING_DRIVER / PROBLEM_28
TEMP_FASTBOOT_CURRENT_DRIVER_STATE = INSTALLED_SIGNED_EXACT_COVERAGE_LIVE_REENUM_NOT_VALIDATED
TEMP_FASTBOOT_DRIVER_MISSING = YES_DURING_FIRST_ATTEMPT
TEMP_FASTBOOT_DRIVER_NOW_READY = YES_DRIVER_STORE_READY_LIVE_REENUM_NOT_VALIDATED
POTATONV_TIMEOUT = YES_DRIVER_FAILURE_EXPLAINS_OBSERVED_WAIT_TIMEOUT_WITH_HIGH_CONFIDENCE
POTATONV_TIMEOUT_EXPLAINED_BY_MISSING_DRIVER = YES_WITH_HIGH_CONFIDENCE
```
