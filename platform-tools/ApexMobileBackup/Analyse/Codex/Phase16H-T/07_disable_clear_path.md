# Disable and clear path

## Userspace control with enabled=0

`ptrace_hbp_fill_attr_ctrl()` sets `attr.disabled=1` and returns before
revalidating type/length. `modify_user_hw_breakpoint()` first calls
`perf_event_disable()` and then stores the disabled bit.

If the event is active, the perf path is:

```text
perf_event_disable
  -> _perf_event_disable / __perf_event_disable
  -> event_sched_out
  -> event->pmu->del
  -> hw_breakpoint_del
  -> arch_uninstall_hw_breakpoint
  -> hw_breakpoint_control(HW_BREAKPOINT_UNINSTALL)
  -> write_wb_reg(BCR, slot, 0)
```

If the tracee context is not active, no BCR is currently installed; setting
`bp->attr.disabled=1` prevents installation on the next schedule-in.

PRA evidence:

- `arch/arm64/kernel/ptrace.c:225-254,322-344`
- `kernel/events/hw_breakpoint.c:433-475,580-600`
- `kernel/events/core.c:1544-1574,1716-1819`
- `arch/arm64/kernel/hw_breakpoint.c:275-300`

Exact C33 disassembly confirms every architecture-specific edge: disable is
called before the attribute update, PMU delete calls
`arch_uninstall_hw_breakpoint`, and uninstall writes a literal zero through
`write_wb_reg` to the selected control register.

The cached architecture control is not rebuilt on the disabled early-return.
GETREGSET may consequently continue to report `0x41e4`. Safe-disable evidence
is SETREGSET success plus the perf/uninstall path and a subsequent no-retrap
handoff, not a full-zero GETREGSET word.

```text
USER_CTRL_ZERO_DISABLE_PATH = PERF_DISABLE_THEN_DISABLED_ATTRIBUTE
HARDWARE_BCR_CLEAR_EXPECTED = YES_IF_INSTALLED_OTHERWISE_ALREADY_ABSENT
CLEAR_SEQUENCE_CONFIDENCE = HIGH
GETREGSET_CTRL_ZERO_NOT_REQUIRED_FOR_SAFE_DISABLE = YES
```
