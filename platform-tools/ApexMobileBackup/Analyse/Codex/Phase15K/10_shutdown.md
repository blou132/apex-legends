# Shutdown and restoration

The guest network state was recorded as airplane mode off, Wi-Fi on, and mobile
data on. Isolation was confirmed before the final SystemUI preflight: airplane
mode was on, Wi-Fi and mobile data were off, connectivity reported no active
default network, and the route table had no default route.

After the preflight stop, the exact initial network state was restored. Apex
had no process because it was never launched. The AVD accepted a clean emulator
shutdown request, ADB reported no remaining endpoint, and the host emulator
processes exited.

```text
NETWORK_RESTORED = YES
AVD_SHUTDOWN_CLEAN = YES
NO_ADB_ENDPOINT_REMAINING = YES
NO_EMULATOR_PROCESS_REMAINING = YES
```
