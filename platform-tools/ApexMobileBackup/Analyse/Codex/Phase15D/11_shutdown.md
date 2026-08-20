# Shutdown and restoration

After the bounded observation and the two one-shot diagnostics:

1. Apex was force-stopped and its process was confirmed absent.
2. Guest airplane mode was restored from `1` to its initial value `0`.
3. Guest Wi-Fi was restored from `0` to its initial value `1`.
4. Guest mobile data was restored from `0` to its initial value `1`.
5. The AVD received a clean emulator shutdown request.
6. Final host checks found no emulator ADB endpoint and no emulator process.

No temporary debug property was set, so none required cleanup. Apex and both
OBBs remain installed in the AVD as required; no uninstall, data clear, or wipe
occurred.

```text
APEX_FORCE_STOP_CONFIRMED = YES
GUEST_NETWORK_RESTORED = YES
EMULATOR_ENDPOINT_REMOVED = YES
EMULATOR_PROCESS_STOPPED = YES
AVD_SHUTDOWN_CLEAN = YES
```
