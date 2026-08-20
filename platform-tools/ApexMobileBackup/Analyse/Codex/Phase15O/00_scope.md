# Phase15O scope

Date: 2026-08-20

Phase15O authorized one bounded offline Apex launch on the preserved
`ApexPhase9Lab` AVD with the command-line-only `-gpu host` override. The launch
was gated behind device, WHPX, boot, SystemUI, artifact/cache, and network
checks.

The mandatory ADB inventory found no physical device. An initial harness boot
attempt encountered the normal transient ADB `offline` state before reaching
boot completion. It launched no application, changed no network state, and was
cleaned up. The corrected preflight cold-booted the AVD in read-only mode and
waited the required 60 seconds.

At that boundary, `dumpsys window windows` exposed a visible
`Application Not Responding: com.android.systemui` system alert. The explicit
stop condition was applied. Apex was never launched, no ANR interaction was
sent, and all later runtime checks were not reached.

Raw emulator output and the targeted window dump remain ignored under
`Analyse/LocalInputs/Phase15O/`. No screenshot, UI XML, binary, account data,
token, identifier, request content, or raw log is published.
