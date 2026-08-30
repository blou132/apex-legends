# Next step

The current blocker is narrower than Phase16H: independent root ARM64 attach
and register-set reading work, but bounded function-entry/register observation
has not been demonstrated.

```text
CURRENT_BLOCKER = ROOT_PTRACE_FUNCTION_ENTRY_OBSERVATION_NOT_YET_VALIDATED
NEXT_STEP = VALIDATE_BOUNDED_FUNCTION_ENTRY_REGISTER_OBSERVATION_ON_DISPOSABLE_ARM64_TARGET
```

A future separately authorized phase should extend the clean-room tracee with
one known noinline function, then test one bounded software-breakpoint or other
read-only-equivalent function-entry workflow. It must restore any temporarily
changed instruction before detach, prove the tracee continues, keep SELinux
enforcing, and remain entirely separate from Apex. Until that succeeds,
Phase16I callback tracing is not ready.
