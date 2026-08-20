# Phase14 scope

Date: 2026-08-20

## Objective

Use only Android's native diagnostic facilities on the secondary Huawei lab to
seek read-only evidence for loaded Apex libraries and native startup frames.

## Authorized boundary

- The preserved Samsung was excluded. The Huawei was the only connected ADB
  endpoint and every device command selected it explicitly.
- The client ran with Internet unavailable. USB ADB remained available.
- The package, APK, OBBs, permissions, data, certificates, and binaries were
  unchanged.
- No root, hook, debugger attach, manual ptrace, patch, SELinux bypass,
  authentication, backend, or external service was used.
- Only `debuggerd -b` was attempted. `showmap` was absent.
- Full device output remains ignored under `LocalInputs/Phase14/`.

## Stop rule

The first `debuggerd -b` request failed at the operating-system diagnostic
boundary. The remaining snapshots were not attempted and no escalation was
performed.
