# Detach and tracee health

No detach was attempted after the unexpected readback. The stopped disposable
tracee was terminated for safety, as required by the failure policy. No test
process remained afterward.

```text
PTRACE_DETACH_RESULT = NOT_PERFORMED_SAFETY_TERMINATED
TRACEE_CONTINUED_AFTER_DETACH = NO_NOT_DETACHED
TRACEE_FUNCTION_RESULT_STILL_CORRECT = NO_NOT_EXECUTED
TRACEE_TERMINATED_CLEANLY = NO_PTRACE_SAFETY_KILL_USED
TRACEE_TERMINATED_FOR_SAFETY = YES
```
