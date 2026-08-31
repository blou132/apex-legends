# Entry trap and caller

No entry breakpoint was armed, so no hardware trap, PC, LR, or entry register
state was captured. The Phase16L static caller remains the only supported
caller model; Phase16M neither confirms nor invalidates it.

```text
CREATEDOLPHIN_ENTRY_TRAP = NOT_EXECUTED
ENTRY_SIGTRAP_CODE = NOT_EXECUTED
ENTRY_SIGTRAP_HW_CLASSIFICATION = NOT_EXECUTED
ENTRY_PC_MATCH = NOT_EXECUTED
CREATEDOLPHIN_LR_CAPTURED = NO
CALLER_RETURN_ADDRESS_MATCH_STATIC_SITE = UNKNOWN
CALLER_MODULE = UNKNOWN
CALLER_MODULE_RELATIVE = UNKNOWN
RUNTIME_STATIC_CALLSITE_CONFIRMED = UNKNOWN
```
