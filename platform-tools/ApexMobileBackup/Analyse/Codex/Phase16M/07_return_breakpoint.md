# Return breakpoint

The entry trap was not reached, so the sequential entry-to-return transition
was not attempted. There was no active hardware state to disable during
cleanup.

```text
ENTRY_BREAK_DISABLE_RESULT = NOT_ARMED
RETURN_BREAK_SET_RESULT = NOT_EXECUTED
RETURN_BREAK_ADDRESS_MATCH = NOT_EXECUTED
RETURN_BREAK_CTRL_MATCH = NOT_EXECUTED
RETURN_BREAK_DISABLE_RESULT = NOT_ARMED
```
