# Restoration and shutdown

After the 30-second success-stability observation:

1. Apex was force-stopped and its process absence was confirmed.
2. Airplane mode, Wi-Fi, and mobile-data settings were restored to their exact
   initial values.
3. The AVD received a graceful emulator kill request.
4. A subsequent inventory showed no ADB endpoint.
5. A host process check showed no emulator process.

```text
APEX_FORCE_STOP_CONFIRMED = YES
NETWORK_RESTORED = YES
AVD_SHUTDOWN_CLEAN = YES
FINAL_ADB_ENDPOINTS = NONE
FINAL_EMULATOR_PROCESSES = NONE
```
