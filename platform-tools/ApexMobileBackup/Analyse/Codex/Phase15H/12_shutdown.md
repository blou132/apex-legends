# Shutdown and restoration

At the end of the bounded observation, Apex was force-stopped and process
absence was confirmed. Airplane mode, Wi-Fi, and mobile-data settings were
restored exactly to the recorded initial values. The AVD received a normal
`emu kill`; its endpoint disappeared and no emulator process remained.

The final independent check also found an empty ADB inventory and no emulator
process. No debug property was used.

```text
APEX_FORCE_STOP_CONFIRMED = YES
NETWORK_RESTORED = YES
FINAL_AIRPLANE_MODE = OFF
FINAL_WIFI = ON
FINAL_MOBILE_DATA = ON
TEMPORARY_DEBUG_PROPERTY = NONE
AVD_SHUTDOWN_CLEAN = YES
NO_ADB_ENDPOINT_REMAINING = YES
NO_EMULATOR_PROCESS_REMAINING = YES
```
