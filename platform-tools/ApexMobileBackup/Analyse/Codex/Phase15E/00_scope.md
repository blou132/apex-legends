# Phase15E scope

Date: 2026-08-20

## Objective

Audit the exact APK's official shell-profileability policy, inventory locally
installed host profiling tools, and reanalyze the existing Phase15D log without
performing another runtime operation.

## Boundary

- The exact local-only `base.apk` was read and hashed without modification.
- The binary Android manifest was decoded locally with `aapt2`.
- Host profiler discovery was read-only; nothing was installed or executed
  against a process.
- Only the existing ignored Phase15D log was searched and correlated.
- No AVD was booted, no Apex process was launched, and no phone or ADB command
  was used.
- No network request, root, debugger, `showmap`, `debuggerd`, simpleperf or
  Perfetto capture, LLDB, Frida, ptrace, hook, patch, DNS change, backend,
  certificate, or authentication operation occurred.

All raw inputs remain under ignored `LocalInputs` paths. Published metadata uses
times relative to Apex process creation and excludes PIDs, endpoints, private
paths, request queries, headers, bodies, and identifiers.
