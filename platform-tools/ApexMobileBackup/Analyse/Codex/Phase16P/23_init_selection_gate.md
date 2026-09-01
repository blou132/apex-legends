# Init selection gate

In the readable immediate predecessor block, dispatch occurs only when a local
status bit in `W23` is clear and owner byte `+0x45` is zero. No immediate
interface null check appears; the protected prelude must establish the
non-null invariant. The Init return bit zero selects failure versus the
slot-`+0x28` success follow-up.

```text
REAL_DOLPHIN_INIT_SELECTION_GATE = W23_EQ_0_AND_OWNER_PLUS_0x45_EQ_0; NON_NULL_INVARIANT_FROM_OPAQUE_PRELUDE; RETURN_BIT0_GATES_SUCCESS
```
