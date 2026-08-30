# Disabled versus active kernel path

## SET order

The audited Huawei/PRA ARM64 ptrace path copies and applies the slot address
before it copies and applies the slot control. Exact C33 disassembly confirms
the same call order:

1. `ptrace_hbp_set_addr`;
2. `ptrace_hbp_set_ctrl`.

Therefore `HW_BREAK_SLOT_SET_ORDER = ADDRESS_THEN_CONTROL`.

## Disabled control

Control zero sets the generic perf breakpoint attribute to disabled. The
disabled branch returns before hardware-breakpoint validation and before the
event is enabled. It updates generic attributes but does not rebuild the new
target into the active architecture breakpoint state.

- `DISABLED_PATH_VALIDATION_SKIPPED = YES`
- `DISABLED_PATH_ARCH_STATE_MATERIALIZED = NO` for the new address

This is the probable explanation for the H-U non-retained address. It is not
promoted to confirmed because H-U did not log its exact returned address.

## Active control

With address and active `0x000001e5` in the same 24-byte request, address is
applied first and control then enables the path. Validation rebuilds the ARM64
architecture state, applies Huawei SSC normalization, and enables the perf
event. The cached GETREGSET control is expected to be `0x000041e4`.

Result: `ACTIVE_CONTROL_TRIGGERS_FULL_VALIDATION = YES`.
