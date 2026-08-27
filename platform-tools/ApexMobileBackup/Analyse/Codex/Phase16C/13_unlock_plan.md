# Future unlock preparation plan

This checklist deliberately stops before any destructive action.

1. Acquire the exact PRA-LX1 `8.0.0.364(C33)` / `05014GCW` firmware from an
   auditable source.
2. Hash the untouched package, inspect its metadata, and extract exact stock
   `KERNEL.img`, `RAMDISK.img`, and `RECOVERY_RAMDISK.img` locally.
3. Validate that each image belongs to the same C33 build and prepare a tested
   restoration route. Do not use C432 or another CUST as a substitute.
4. Verify HiSuite/eRecovery availability for this device without changing it.
5. Obtain a reliable exact PRA-LX1 motherboard/testpoint reference and compare
   it with the physical board revision before opening the phone.
6. Prepare signed Huawei USB COM drivers, a known-good data cable, stable host
   power, and a comfortably charged battery.
7. Reconfirm the ignored Apex APK/OBBs and their hashes.
8. Request a separate explicit destructive-phase authorization that accepts
   userdata wipe and physical-device risk.

Only after all eight items pass should a future operator decide whether to
enter testpoint mode or execute PotatoNV. This document does not authorize that
decision.
