# Restoration and single-step

The software write failed before changing memory. Consequently the exact
original word remained present, no PC correction was needed, and no
single-step was attempted.

```text
ORIGINAL_INSTRUCTION_RESTORED = NOT_APPLICABLE_WRITE_FAILED
RESTORE_READBACK_MATCH = NOT_APPLICABLE_WRITE_FAILED
PC_CORRECTION_REQUIRED = NOT_DETERMINED_NO_TRAP
PC_CORRECTION_APPLIED = NO
SINGLESTEP_RESULT = NO_NOT_REACHED
ORIGINAL_INSTRUCTION_EXECUTED = NO_NOT_REACHED
```

The hardware test did not modify executable memory. When its debug-control
state could not be restored exactly, the stopped disposable tracee was killed
instead of being resumed or detached.
