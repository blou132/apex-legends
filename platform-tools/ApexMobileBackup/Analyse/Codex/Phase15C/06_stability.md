# Stability

After boot and guest validation, the AVD remained running for an additional 30
seconds.

```text
ADB_ENDPOINT_STATE_AFTER_30S = device
BOOT_COMPLETED_AFTER_30S = 1
EMULATOR_PROCESS_ALIVE_AFTER_30S = YES
PHYSICAL_DEVICE_PRESENT_AFTER_30S = NO
FATAL_HYPERVISOR_LOG_MATCHES = 0
AVD_STABLE_30S = CONFIRMED YES
```

The fatal-error check was limited to WHPX/hypervisor fatal, panic, failure, and
error patterns in the complete local-only emulator logs.
