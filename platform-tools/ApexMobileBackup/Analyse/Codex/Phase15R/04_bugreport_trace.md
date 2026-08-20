# Bugreport and trace selection

Exactly one `adb bugreport` command was issued. ADB successfully transferred a
valid archive of 4,325,738 bytes containing 203 entries. The PowerShell wrapper
classified ADB's transfer-progress text on stderr as an error after the file
had arrived; this did not invalidate the archive and no retry was made.

The generic `VM TRACES AT LAST ANR` block in the later bugreport points to a
different application startup ANR. It was deliberately rejected. The exact
SystemUI trace was selected from `/data/anr` by its `17:57:15` timestamp,
subject, process name, PID, and `KeyguardService` reason.

```text
BUGREPORT_CAPTURED = YES
BUGREPORT_ATTEMPTS = 1
SYSTEMUI_ANR_TRACE_PRESENT = YES
TRACE_MATCH = EXACT_TIMESTAMP_PROCESS_AND_REASON
```

The archive and extracted trace remain local-only.
