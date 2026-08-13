# Phase9C scope

Date: 2026-08-13

## Objective

Phase9C uses a secondary Huawei ARM64 phone as a clean runtime lab after the x86_64 emulator could not boot. The preserved Samsung Apex phone is excluded from every runtime operation.

## Safety boundary

- ADB identified the Huawei by its `PRA-LX1` model and every phone command used its explicit serial internally.
- No command targeted the preserved Samsung.
- No private state, account, token, cookie, database, preference, or device identifier was copied.
- Only the verified local base APK and two verified OBB copies were installed on the Huawei test device.
- The first run used no hook, root, patch, proxy, certificate change, account, or bypass.
- Wi-Fi and mobile data were blocked on the Huawei before launch and restored to their initial state after the test.
- Raw logcat and the screen capture remain local-only.

## Result

The ARM64 APK installed and the client process plus `GameActivity` remained active for more than 90 seconds. The display stayed black and the first actionable error was a DNS/backend resolution failure under the deliberate network block. No native crash occurred.

```text
FINAL_GATE = B HUAWEI_CLIENT_STARTS_THEN_BACKEND_FAILURE
```
