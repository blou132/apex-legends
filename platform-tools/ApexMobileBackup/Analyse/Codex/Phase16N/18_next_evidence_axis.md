# Next evidence axis

The strongest remaining non-bypass axis is a PC-only analysis of the state
that produces the argument at the exact static CreateDolphin callsite. This can
test why the valid-looking return path is discarded without requiring runtime
thread ownership or protector interaction.

`NEXT_EVIDENCE_AXIS = PC_ONLY_CREATEDOLPHIN_CALLSITE_PRODUCER_STATE`
