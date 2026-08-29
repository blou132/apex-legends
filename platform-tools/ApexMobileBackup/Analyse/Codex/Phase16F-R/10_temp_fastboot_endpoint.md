# Temporary fastboot endpoint

Three independent evidence layers identify `VID_18D1/PID_D00D` as the normal
next state after the RAM upload:

1. PotatoNV `Fastboot.cs` hardcodes that VID/PID and waits for it immediately
   after the VCOM upload.
2. Windows physically enumerated `Fastboot2.0` with that exact ID in the
   Phase16F upload interval.
3. The signed Huawei `hw_goadb.inf` package contains exact D00D coverage named
   `Android Sooner Single ADB Interface` and binds it to WinUSB.

The INF description does not mean the normal Android ADB daemon was running.
For PotatoNV, the important property is that WinUSB/libusb can open the
temporary RAM fastboot transport.

The first endpoint was real but unusable by PotatoNV because it had no driver
binding. The current package state is ready in the driver store, but a live
D00D binding remains unvalidated without another separately authorized
physical run.

## `PHONE Unlocked` evidence

No timestamped local Phase16F photo or transcript record preserves a
`FASTBOOT&RESCUE MODE / PHONE Unlocked` screen during the first upload. Its
role cannot be inferred. If a provenance-preserving artifact is later
supplied, a temporary RAM-fastboot interpretation may be evaluated, but it
must never be promoted to permanent unlock without the later NV/code and
unlock-command evidence.

```text
FIRST_ATTEMPT_TEMP_FASTBOOT_VIDPID = VID_18D1/PID_D00D
TEMP_FASTBOOT_ENDPOINT_EXPECTED = YES
PHONE_UNLOCKED_SCREEN_ROLE = NOT_CLASSIFIABLE_NO_PRESERVED_TIMESTAMPED_ARTIFACT
```
