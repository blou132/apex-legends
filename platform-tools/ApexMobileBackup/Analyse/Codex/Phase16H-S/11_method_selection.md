# Breakpoint method selection

Software executable-memory writes are unavailable through this ptrace path on
the PRA-LX1. The ARM64 hardware-breakpoint regset exists, but its vendor control
readback and restoration semantics differ from the validated generic Android
4.4 UAPI and were not safely resolved.

```text
ROOT_PTRACE_SOFTWARE_BREAKPOINT_READY = NO
ROOT_PTRACE_HARDWARE_BREAKPOINT_READY = NO
ROOT_PTRACE_FUNCTION_ENTRY_CAPABILITY = NO_NOT_PROVEN
SELECTED_PHASE16I_BREAKPOINT_METHOD = UNRESOLVED
ROOT_CALLBACK_TRACE_PRECONDITION = NOT_SATISFIED
PHASE16I_READY = NO
FINAL_GATE = C REGISTER_ATTACH_ONLY_BREAKPOINT_UNAVAILABLE
```
