# Phase15G scope

Date: 2026-08-20

## Question

Determine whether the unchanged local OBB ZIP-entry CRC32 validation completes
after the previous 60-second observation bound.

## Authorized runtime boundary

- The mandatory ADB inventory contained no physical device.
- Only the existing `ApexPhase9Lab` AVD was used.
- The AVD booted with snapshot load/save disabled and without wipe.
- The already installed APK and OBBs were not reinstalled or modified.
- Apex was launched exactly once, with no account or login input.
- The guest was isolated before launch and remained offline during observation.
- Waiting was bounded to 600 seconds after downloader entry and stopped on the
  first confirmed success transition.

No root, patch, hook, mapping read, debugger, profiler, tracing, process
sampling, network request test, DNS redirect, local backend, certificate, or
authentication action was used. Both phones remained excluded.

Raw logcat, activity dumps, endpoint data, process identifiers, and the local
observation script remain under ignored `LocalInputs`. Only relative times and
cleaned state transitions are committed.
