# Hardware install and address path

## Install call graph

```text
ptrace_hbp_set_ctrl
  -> modify_user_hw_breakpoint
  -> validate_hw_breakpoint
  -> arch_validate_hwbkpt_settings
  -> perf_event_enable
  -> event_sched_in / PMU add
  -> hw_breakpoint_add
  -> arch_install_hw_breakpoint
  -> hw_breakpoint_control(HW_BREAKPOINT_INSTALL)
```

PRA `hw_breakpoint_control()` writes the cached address to the BVR and then
writes the encoded control to the BCR as:

```c
reg_enable ? ctrl | 0x1 : ctrl & ~0x1
```

Relevant source is `arch/arm64/kernel/hw_breakpoint.c:227-300`, with perf PMU
add at `kernel/events/hw_breakpoint.c:580-600`.

Exact C33 disassembly confirms:

- `arch_install_hw_breakpoint -> hw_breakpoint_control`.
- The cached address is passed to `write_wb_reg` before the control.
- The install path ORs bit 0 when thread breakpoints are not globally disabled.
- The disable-global branch clears bit 0 instead.

The expected installed control is therefore `0x000041e5`. This is
source-and-binary-derived only; Phase16H-S did not read a hardware BCR and did
not schedule the stopped tracee after programming.

The address path is:

```text
ptrace_hbp_set_addr
  -> bp->attr.bp_addr
  -> modify_user_hw_breakpoint
  -> arch_validate_hwbkpt_settings / cached info->address
  -> hw_breakpoint_control / BVR write on install
```

The exact C33 call graph and Phase16H-S matching address readback agree with
this path.

```text
HARDWARE_ENABLE_FORCED_ON_INSTALL = YES
EXPECTED_ACTUAL_BCR = 0x000041e5_SOURCE_DERIVED_ONLY
ADDRESS_PATH_EXPLAINED = YES
```
