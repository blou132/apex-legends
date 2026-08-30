# Phase16H-T scope

Date: 2026-08-30

This phase is a PC-only reconstruction of the PRA-LX1 ARM64 hardware-breakpoint
ABI observed in Phase16H-S. No ADB command was issued, no phone was connected or
queried, and no runtime test was executed.

## Included

- Decode requested control `0x000001e5` and returned control `0x000041e4`.
- Trace GETREGSET, perf cached state, install, disable, exit, and detach paths.
- Compare PRA 8.x, Lineage hi6250, and Android common ARM64 source.
- Inspect the exact archived C33 `KERNEL.img` non-destructively on the PC.
- Design, but do not execute, one future disposable-target test.

## Excluded

- ADB, fastboot, USB discovery, or any device command.
- Apex installation, launch, modification, tracing, or network activity.
- Flashing, patching, booting, or changing the archived kernel image.
- Claiming that a community tree is byte-identical to the exact C33 kernel.

```text
PHONE_TOUCHED = NO
APEX_INSTALLED = NO
APEX_LAUNCHED = NO
```
