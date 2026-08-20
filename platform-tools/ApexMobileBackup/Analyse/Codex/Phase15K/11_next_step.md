# Next step

The exact Apex dialog remains the narrow runtime target, but another Apex run
is justified only after a prelaunch boot remains free of a SystemUI ANR through
the required stability window.

A future retry should preserve the same AVD and repeat the non-interactive
preflight. If SystemUI stays clear and the baseline XML capture works, perform
the single bounded offline Apex launch and capture the hierarchy immediately
when the third Apex window appears. Do not click the dialog.

If the SystemUI ANR recurs, stop again and investigate emulator/SystemUI
stability read-only rather than obscuring or dismissing it.

```text
NEXT_RUNTIME_METHOD = RETRY_DIALOG_CAPTURE_ONLY_AFTER_CLEAN_SYSTEMUI_PREFLIGHT
FINAL_GATE = F SYSTEMUI_OVERLAY_PRESENT_STOP
```
