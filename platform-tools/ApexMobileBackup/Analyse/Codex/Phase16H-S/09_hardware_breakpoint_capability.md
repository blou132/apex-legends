# ARM64 hardware-breakpoint capability

The phone runs an AArch64 4.4.23+ vendor kernel. Android common 4.4-o source
revision `edd606b9fcdd6d1d81ebe04bd0738d85283767ce` was used only as the UAPI
reference. It defines `NT_ARM_HW_BREAK = 0x402` and a 264-byte
`user_hwdebug_state` with 16 address/control records.

Device behavior confirmed the regset exists and reports six execution slots
with debug architecture value 6. All reported slots were initially clear.

```text
ARM64_HW_BREAK_REGSET_SUPPORTED = YES
HW_BREAK_REGSET_LENGTH = 264
HW_EXEC_BREAKPOINT_SLOTS = 6
HW_BREAKPOINT_INITIAL_STATE_CLEAR = YES
```

The generic Android control encoding for four-byte EL0 execution was
`0x000001e5`. Exact Huawei vendor-kernel control semantics remain unresolved.
