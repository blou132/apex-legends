# Breakpoint cleanup

On timeout, the tracer stopped the selected TID and issued the single explicit
slot-0 disable operation. The disable succeeded. With state confidently
disabled, ptrace detach succeeded. Apex was then force-stopped and all package
TIDs exited.

The helper and watcher were removed from `/data/local/tmp`. No Apex, tracer, or
watcher process remained.

```text
APEX_HW_BREAK_DISABLE_RESULT = SUCCESS
APEX_PTRACE_DETACH_RESULT = SUCCESS
APEX_FORCE_STOP_RESULT = SUCCESS
APEX_PROCESS_REMAINING = NO
```
