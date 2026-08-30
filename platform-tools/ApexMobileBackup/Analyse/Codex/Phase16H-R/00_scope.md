# Phase16H-R scope

Date: 2026-08-30

This phase was limited to the rooted PRA-LX1. It diagnosed the existing Frida
failure and tested native attach capability only against disposable ARM64
processes. Apex remained uninstalled and unlaunched.

The phase did not access Samsung hardware, weaken SELinux, modify sepolicy,
install a Magisk module, enable Zygisk, change kernel settings, intercept
network traffic, contact a backend, or handle credentials.

Raw logs, tombstones, downloaded tool archives, virtual environments, build
outputs, and device identifiers remain local-only under the gitignored
`Analyse/LocalInputs/Phase16H-R` tree.

`PHASE16H_R_PREFLIGHT = PASS`
