# Hard abort conditions

Phase16F must stop before any physical contact or state-changing command when
any of these conditions is true:

- Phone battery is below 60 percent, is not stable, or temperature is abnormal.
- Host is not on stable power or may sleep during the bounded operation.
- USB enumeration drops, changes unexpectedly, or the cable/port is unstable.
- A hub is introduced into the active USB route.
- An unknown COM/USB device appears or the expected Huawei bootrom device does
  not enumerate exactly as planned.
- The driver archive/hash, INF version, catalog signature, or device binding
  differs from the Phase16E record.
- PotatoNV archive or executable hash differs from Phase16D/Phase16E.
- Firmware, kernel, ramdisk, or recovery ramdisk hash differs.
- The connected device is not exact model `PRA-LX1`, board `hi6250`, and build
  `PRA-LX1 8.0.0.364(C33)`.
- The observed bootloader, OEM, FRP, verified-boot, or FBLOCK state differs from
  the future phase's expected baseline.
- The actual board revision, orientation, shield, connector, screw, pad-row,
  silkscreen, or testpoint neighborhood does not match all references.
- The battery cannot be disconnected safely before board work where required.
- The phone or host overheats, the phone reboots unexpectedly, or any tool
  reports a write/error outside the approved state machine.
- Exact C33 recovery material is inaccessible or cannot be rehashed.
- The owner has not completed a final backup review and explicitly accepted
  loss of all userdata immediately before the destructive phase.
- Any unrelated physical Android device is connected.

An abort leaves the device untouched whenever possible. It is never a reason
to improvise a flash, erase, FBLOCK change, alternate testpoint, unsigned
driver, or unpinned tool.

```text
ABORT_CONDITIONS_DEFINED = YES
```
