# Temporary fastboot live binding

The D00D transition completed before the later PnP snapshot, so no direct
present-device problem-code sample was retained. Functional evidence is
decisive: the pinned PotatoNV source waits only for `VID_18D1/PID_D00D`
through libusb, and the operation advanced from `Waiting for any device` to
`Connecting`, device information, and NV writes.

The stored Windows devnode identifies `oem77.inf`, service `WinUSB`, version
`2.0.5.0`, and exact `18D1:D00D` coverage. This validates the previously
untested live binding at the functional level.

```text
TEMP_FASTBOOT_ENUMERATED = YES_PROVEN_BY_EXACT_POTATONV_D00D_PATH
TEMP_FASTBOOT_DRIVER_BOUND = YES_OEM77_WINUSB_FUNCTIONAL
TEMP_FASTBOOT_LIBUSB_VISIBLE = YES
TEMP_FASTBOOT_PROBLEM_CODE = NONE_BLOCKING_FUNCTIONALLY
```
