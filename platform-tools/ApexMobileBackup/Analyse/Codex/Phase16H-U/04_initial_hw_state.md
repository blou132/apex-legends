# Initial hardware-breakpoint state

Root `PTRACE_ATTACH` succeeded and produced the expected stop. `NT_PRSTATUS`
returned 272 bytes. `NT_ARM_HW_BREAK` returned 264 bytes with six execution
slots, and all six initial controls were zero.

Only slot 0 was selected. No watchpoint slot or other execution slot was
modified.

```text
PTRACE_ATTACH_RESULT = SUCCESS
REGISTER_SET_READY = YES
HW_BREAK_INITIAL_READ_OK = YES
HW_EXEC_BREAKPOINT_SLOTS = 6
```
