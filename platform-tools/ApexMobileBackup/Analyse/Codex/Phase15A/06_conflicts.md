# Hypervisor coexistence audit

## Detected

- Oracle VirtualBox 7.2.4 is installed.
- Its support, networking, and USB kernel drivers are loaded.

## Not detected

- Android Emulator Hypervisor Driver (AEHD)
- Intel HAXM
- VMware products or matching services

VirtualBox is recorded as a potential coexistence consideration, not as a
proven blocker. Current Android Emulator acceleration already has a simpler
demonstrated cause: WHPX is disabled and AEHD is absent. No VirtualBox process,
service, driver, or product was stopped, reconfigured, or removed.

```text
POTENTIAL_HYPERVISOR_CONFLICTS = VIRTUALBOX_PRESENT_NOT_PROVEN_BLOCKING
```
