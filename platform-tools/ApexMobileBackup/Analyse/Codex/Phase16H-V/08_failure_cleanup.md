# Failure cleanup

The tracer tracks attach, stopped/running, armed, disable-attempted, disabled,
and gate-created state explicitly.

On any failure:

1. remove the start gate if it was created;
2. stop the tracee if it is running;
3. if an active breakpoint may be armed and disable has not yet been attempted,
   make exactly one explicit disable attempt;
4. terminate the disposable tracee through ptrace kill or SIGKILL;
5. never detach from uncertain hardware-debug state.

The `disable_attempted` guard prevents cleanup from issuing a second disable
after a failed main-path disable. If active SET reports failure before the
tracer can mark the slot armed, process termination remains the final kernel
cleanup boundary.

All waits are bounded. The design does not continue after malformed layout,
unexpected slot count, non-clear initial controls, readback mismatch, wrong
signal, wrong PC, or wrong argument registers.

Result: `FAILURE_EXIT_CLEANUP_READY = YES`.
