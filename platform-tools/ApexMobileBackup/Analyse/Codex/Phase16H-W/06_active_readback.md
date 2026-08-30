# Active readback

Immediate GETREGSET returned the same runtime target address and a 264-byte
hardware state. Requested active control `0x000001e5` returned as Huawei cached
control `0x000041e4`, exactly as resolved in Phase16H-T and accepted by the
Phase16H-V tracer.

The exact requested and returned addresses are retained only in the local raw
log.

- `ACTIVE_ADDRESS_READBACK_MATCH = YES`
- `ACTIVE_CTRL_READBACK = 0x000041e4`
- `ACTIVE_CTRL_NORMALIZATION_MATCH = YES`
- `HW_BREAKPOINT_ARMED_FOR_TEST = YES`
