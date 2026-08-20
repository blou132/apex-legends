# Post-gate timeline

1. Mandatory ADB inventory: no endpoint.
2. WHPX capability check: passed.
3. `ApexPhase9Lab` host-mode read-only boot: completed.
4. SystemUI `0 s` checkpoint: clear at `+1.731 s`.
5. SystemUI `30 s` checkpoint: visible ANR at `+32.169 s`.
6. Hard stop and emulator shutdown: completed.

No package validation, network isolation, Apex launch, GameActivity resume, or
post-graphics timeline exists for this run.

```text
FIRST_NEW_POST_GRAPHICS_STAGE = NOT_REACHED
FIRST_UNRESOLVED_TRANSITION = SYSTEMUI_PREFLIGHT_TO_APEX_LAUNCH_BLOCKED
```
