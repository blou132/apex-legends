# SSC normalization

PRA `arch_build_bp_info()` rebuilds the architecture control from the perf
attributes. Under `CONFIG_HAVE_HW_BREAKPOINT_ADDR_MASK`, it assigns the address
mask and unconditionally assigns:

```c
info->ctrl.ssc = ARM_SSC_NON_SECURE;
```

Relevant PRA lines are `arch/arm64/kernel/hw_breakpoint.c:390-485`; the SSC
assignment is at lines 454-471. The header defines `ARM_SSC_NON_SECURE=1`, and
`encode_ctrl_reg()` shifts `ssc` by 14.

Lineage has the same behavior at
`arch/arm64/kernel/hw_breakpoint.c:425-532`. The exact C33 IKCONFIG confirms
`CONFIG_HAVE_HW_BREAKPOINT_ADDR_MASK=y`, and exact C33 disassembly of
`arch_validate_hwbkpt_settings` contains the equivalent two-bit insertion of
value 1 into the cached SSC field.

Therefore the SSC contribution is exactly:

```text
1 << 14 = 0x00004000
```

```text
SSC_NORMALIZATION_DELTA = 0x00004000
READBACK_PLUS_0X4000_EXPLAINED = YES
```
