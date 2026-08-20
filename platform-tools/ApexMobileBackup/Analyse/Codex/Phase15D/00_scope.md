# Phase15D scope

Date: 2026-08-20

## Objective

Install the exact Apex APK and OBB artifacts in the existing translated
`ApexPhase9Lab` AVD, isolate the guest from the network before first launch,
and observe one bounded offline runtime without privileged instrumentation.

## Controlled boundary

- The mandatory initial ADB inventory was empty. No physical device or other
  endpoint was present.
- WHPX and the existing AVD were revalidated. The AVD was booted without
  snapshot load/save and without wipe.
- Only the exact pre-verified APK and two OBB files were installed.
- Guest airplane mode was enabled and Wi-Fi/mobile data were disabled before
  Apex was launched. No active default network or default route remained.
- Apex was launched once, with no account, login input, or backend response.
- Observation was limited to process/activity checks at about +5, +20, and
  +60 seconds, one `showmap` attempt, and one `debuggerd -b` attempt.
- No root, hook, patch, ptrace, debugger attach, certificate change, DNS
  redirection, authentication emulation, or physical-phone command occurred.
- Apex was force-stopped, the initial guest network state was restored, and
  the AVD was shut down cleanly.

Raw logs and the local run harness remain ignored under `LocalInputs/Phase15D/`.
Published files contain no endpoint port, process identifier, user path, token,
private query string, or original binary.
