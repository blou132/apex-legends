# USB drivers

The hash-pinned Phase16E Huawei driver archive was used. Its signed
`hw_usbvcom.inf` package provided `HUAWEI USB COM 1.0` version 2.0.7.1.

During PotatoNV, the RAM bootloader appeared as `Fastboot2.0`
(`VID_18D1/PID_D00D`) with Windows problem code 28. The same validated Huawei
archive already contained signed `hw_goadb.inf` coverage for that exact
temporary endpoint. That single WHCP-signed driver package was added to the
Windows driver store after the timeout. No unrelated driver pack was bound.

```text
VCOM_DRIVER_INSTALLED = YES
VCOM_DEVICE_DETECTED = YES
TEMPORARY_FASTBOOT_DRIVER_INSTALLED = YES_AFTER_FIRST_TIMEOUT
```
