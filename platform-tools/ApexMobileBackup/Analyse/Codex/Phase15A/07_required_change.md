# Required change

## Decision

```text
REQUIRED_CHANGE = ENABLE_WINDOWS_HYPERVISOR_PLATFORM
REBOOT_REQUIRED = YES
FINAL_GATE = B WHPX_ENABLE_REQUIRED
```

The minimum demonstrated missing prerequisite is the disabled
`HypervisorPlatform` feature. Firmware virtualization and SLAT are already
available; enabling the full Hyper-V role, Virtual Machine Platform, AEHD, or a
BIOS change is not currently justified.

## Plan only - not executed

Preferred GUI sequence:

1. Open **Turn Windows features on or off**.
2. Enable **Windows Hypervisor Platform**.
3. Confirm the change and restart Windows when requested.
4. After reboot, run `emulator.exe -accel-check` again.

Official command-line equivalent from an elevated terminal, if deliberately
chosen later:

```powershell
dism.exe /Online /Enable-Feature /FeatureName:HypervisorPlatform /All
```

Then restart Windows. This command was not executed in Phase15A.

Because non-elevated BCD inspection was denied, do not change BCD preemptively.
Only if WHPX remains unavailable after enablement and reboot should an elevated,
read-only `bcdedit /enum {current}` check be performed before considering any
boot-setting change.
