# Trap result

`PTRACE_CONT` was never issued. No hardware breakpoint was armed, no SIGTRAP
was received, and no trap classification or address comparison was possible.

```text
SIGTRAP_RECEIVED = NO_NOT_CONTINUED
SIGTRAP_CODE = NOT_CAPTURED
SIGTRAP_ADDRESS_MATCH = NOT_REACHED
EXACT_FUNCTION_ENTRY_TRAP = NO_NOT_REACHED
```
