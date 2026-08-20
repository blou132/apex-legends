# Shutdown

The discovered emulator endpoint accepted `emu kill`. The endpoint and Windows
emulator/QEMU processes then disappeared without fallback termination.

```text
EMU_KILL_EXIT_CODE = 0
EMU_KILL_ACKNOWLEDGED = YES
EMULATOR_ENDPOINT_PRESENT_AFTER_WAIT = NO
EMULATOR_PROCESS_RUNNING_AFTER_WAIT = NO
FORCED_PROCESS_TERMINATION = NO
AVD_SHUTDOWN_CLEAN = CONFIRMED YES
```
