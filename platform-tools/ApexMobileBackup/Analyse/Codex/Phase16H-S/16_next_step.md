# Next step

```text
CURRENT_BLOCKER = SOFTWARE_POKETEXT_EIO_AND_VENDOR_HW_BREAK_CONTROL_ABI_UNRESOLVED
NEXT_STEP = RESOLVE_EXACT_HUAWEI_4_4_HW_BREAK_REGSET_SEMANTICS_PC_ONLY
```

Do not install, launch, or attach to Apex. A future phase should remain PC-only
and seek the exact PRA-LX1 C33 Huawei 4.4 kernel source or another authoritative
description of its `NT_ARM_HW_BREAK` control encoding and disable semantics.
No new device programming attempt is justified until the observed
`0x000041e4` normalization and a provably reversible clear sequence are
explained.

If exact vendor semantics cannot be established, retain Gate C and select a
different, independently validated observation method rather than weakening
SELinux or experimenting on a real application.
