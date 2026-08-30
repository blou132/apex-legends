# Future one-attempt state machine

This state machine is designed but was not executed in Phase16H-V.

| State | Required transition |
| --- | --- |
| `PREFLIGHT` | Exact disposable tracee and target correlation pass. |
| `ATTACHED_STOPPED` | Attach and GPR/HW regset checks pass. |
| `ACTIVE_SET_SUBMITTED` | One slot-0 address plus `0x1e5` SET is issued. |
| `ACTIVE_READBACK_VALID` | Address matches and control is `0x41e4`. |
| `GATE_RELEASED` | Start gate is created only after valid readback. |
| `ENTRY_TRAP` | `TRAP_HWBKPT`, exact address, exact PC, and X0-X7 are captured. |
| `DISABLED` | One same-address/control-zero SET succeeds. |
| `NO_RETRAP_PROVEN` | Second call reaches controlled SIGSTOP without SIGTRAP. |
| `DETACHED` | Detach occurs only after the proven disable path. |
| `POST_DETACH_HEALTHY` | The external bounded runner observes the tracee's post-detach result. |
| `TERMINATED` | The runner terminates the disposable tracee and verifies cleanup. |

Any failed transition enters bounded cleanup and terminates the disposable
tracee. A future phase may consume at most one runtime attempt and requires
separate authorization.

The tracer owns the ptrace states through detach. The future bounded runner
owns post-detach stdout verification, tracee termination, and artifact cleanup;
these are not silently inferred from tracer exit status.

`FUTURE_RUNTIME_ATTEMPT_LIMIT = ONE`.
