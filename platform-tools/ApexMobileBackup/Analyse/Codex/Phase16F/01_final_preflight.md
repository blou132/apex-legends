# Final preflight

The repository was clean on `main` before physical work. The Phase16D/16E
inputs were rehashed before use and matched their pinned SHA-256 values:

- exact PRA-LX1 C33 firmware and extracted stock images;
- PotatoNV v2.2.1 executable and release archive;
- Huawei testpoint driver archive;
- preserved Apex APK, main OBB, and patch OBB.

Normal read-only ADB identified one PRA-LX1 on build
`PRA-LX1 8.0.0.364(C33)` with platform `hi6250`, locked/green verified-boot
baseline, 99 percent battery, and normal temperature. No Samsung endpoint was
present. Full-wipe readiness was confirmed, although no wipe was ultimately
triggered.

```text
INPUT_HASHES_VALID = YES
TARGET_IDENTITY_CONFIRMED = YES
FINAL_WIPE_READINESS = YES
HOST_READY = YES
USB_READY = YES
```
