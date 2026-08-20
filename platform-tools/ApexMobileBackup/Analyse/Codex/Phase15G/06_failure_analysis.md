# Failure analysis

No validation failure occurred. Apex-attributed log lines contain one
validator-success marker and one successful result-1 callback, with no:

- validator false result;
- CRC mismatch/failure;
- ZIP read error;
- I/O error;
- fatal exception;
- process exit during validation.

Unrelated system-process messages were not attributed to Apex. No specific OBB
entry failed, and neither OBB was modified or replaced.

```text
VALIDATION_FAILURE_CLASS = NONE
SPECIFIC_OBB_ENTRY = NOT_APPLICABLE
```
