# Hardware-breakpoint readback

The failure occurred during the disabled address-only pre-step. Consequently,
there is no Phase16H-U control readback for requested `0x000001e5`, and the
expected Huawei normalized value `0x000041e4` was not tested at runtime in this
phase.

Phase16H-S remains the evidence that an atomic address plus active-control
submission returns the address correctly and normalizes control to `0x41e4`.
Phase16H-U does not overwrite that evidence, but it also does not establish a
working execution trap.

```text
HW_BREAK_CTRL_READBACK = NOT_REACHED
HW_BREAK_NORMALIZED_READBACK_EXPECTED = NOT_REACHED
```
