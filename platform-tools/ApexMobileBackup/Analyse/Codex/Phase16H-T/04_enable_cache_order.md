# Enable-cache ordering

## First enable sequence

The ptrace event is created disabled:

1. `ptrace_hbp_create()` sets `attr.disabled=1`.
2. Phase16H-S writes the address first; that update remains disabled.
3. `ptrace_hbp_set_ctrl()` decodes enabled=1 and passes an attribute copy with
   `disabled=0` to `modify_user_hw_breakpoint()`.
4. PRA `modify_user_hw_breakpoint()` disables the current event, copies address,
   type, and length, then calls validation before copying the new disabled bit
   back to `bp->attr`.
5. `arch_validate_hwbkpt_settings()` therefore sees the old
   `bp->attr.disabled=1` and caches `info->ctrl.enabled=0`.
6. `perf_event_enable()` activates the perf event afterward.
7. Only at function end is `bp->attr.disabled` changed to 0.

PRA evidence:

- `arch/arm64/kernel/ptrace.c:184-254,322-365`
- `kernel/events/hw_breakpoint.c:433-475`
- `arch/arm64/kernel/hw_breakpoint.c:484-500`

The exact C33 disassembly reproduces this older ordering. Its
`modify_user_hw_breakpoint` calls disable, updates the attributes, validates,
calls enable, and copies the disabled bit only afterward. Its exact validation
code computes the cached enabled bit as logical-not of the still-old disabled
bit.

Lineage later makes the intent explicit by assigning `bp->attr.disabled=1`
before validation (`kernel/events/hw_breakpoint.c:431-459`), producing the same
cached-zero result.

```text
CACHED_ENABLE_BIT_ZERO_EXPLAINED = YES
```
