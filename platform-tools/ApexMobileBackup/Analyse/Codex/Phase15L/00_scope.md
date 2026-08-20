# Phase15L scope

Date: 2026-08-20

Phase15L performed the single authorized clean-preflight retry on the existing
`ApexPhase9Lab` AVD. Its only runtime objective was to capture the Apex-owned
dialog hierarchy after a 120-second SystemUI stability gate.

The run used no physical Android device, account, application input, root,
hook, patch, profiler, proxy, backend, certificate, or network request. The AVD
was booted without wipe and without snapshot load/save. Apex was launched once.

Raw window/activity dumps, XML, screenshot attempt, APK/DEX/native artifacts,
Ghidra output, and the observation script remain local-only under ignored
`LocalInputs/Phase15L/`. Only sanitized conclusions and machine-readable
metadata are committed.
