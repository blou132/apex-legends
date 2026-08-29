# PotatoNV upstream state machine

The source was inspected at pinned commit
[`ef04f2924e55137a904fb97656fefe8aaa5d954f`](https://github.com/kitsuned/PotatoNV/tree/ef04f2924e55137a904fb97656fefe8aaa5d954f).

## Expected flow

1. `UsbController` identifies DOWNLOAD_VCOM as `VID_12D1/PID_3609`.
2. [`Core.cs`](https://github.com/kitsuned/PotatoNV/blob/ef04f2924e55137a904fb97656fefe8aaa5d954f/PotatoNV-next/Core.cs)
   opens the VCOM port and uploads every image in the selected manifest.
3. `hisi65x_a/manifest.xml` orders `xloader` and then `fastboot`.
4. The VCOM port is closed and PotatoNV logs `Waiting for any device`.
5. [`Fastboot.cs`](https://github.com/kitsuned/PotatoNV/blob/ef04f2924e55137a904fb97656fefe8aaa5d954f/Potato.Fastboot/Potato.Fastboot/Fastboot.cs)
   polls libusb for `VID_18D1/PID_D00D` every 500 ms and throws after counter
   50, approximately 25 seconds.
6. If found, PotatoNV connects, reads device information, then calls its NVME
   write path.
7. The write path sets FBLOCK according to the checkbox and writes WVLOCK and
   USRKEY before optional reboot and code display.

Phase16F stopped at step 5. The log contains no `Connecting`, device
information, NV write, reboot, or generated-code stage. `Disable FBLOCK`
remained unchecked, so no FBLOCK change was requested.

The upstream [README](https://github.com/kitsuned/PotatoNV/blob/ef04f2924e55137a904fb97656fefe8aaa5d954f/README.md)
explains that the board bootloader is uploaded to RAM through DOWNLOAD_VCOM
and then switches to fastboot. Its tested-device table maps PRA to
`Kirin 65x (A)`.

```text
POTATONV_PROFILE = Kirin 65x (A)
FBLOCK_CHANGE_REQUESTED = NO
TEMP_FASTBOOT_ENDPOINT_EXPECTED = YES_UPSTREAM_SOURCE_HARDCODED
FIRST_ATTEMPT_TIMEOUT_ROOT_CAUSE = TEMP_FASTBOOT_NOT_LIBUSB_VISIBLE_DUE_MISSING_WINUSB_DRIVER
```
