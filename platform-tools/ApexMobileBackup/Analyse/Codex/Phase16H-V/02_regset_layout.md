# ARM64 hardware-breakpoint regset layout

The tracer uses the following compile-time-checked layout:

| Field | Offset | Size |
| --- | ---: | ---: |
| `debug_info` | 0 | 4 |
| header pad | 4 | 4 |
| slot 0 address | 8 | 8 |
| slot 0 control | 16 | 4 |
| slot 0 pad | 20 | 4 |
| slot 1 | 24 | 16 |

The header is 8 bytes, `struct hwdebug_register` is 16 bytes, and the full
compatibility state is 264 bytes. The ABI buffer has capacity for 16 slots,
while the PRA-LX1 runtime evidence reports six actual execution slots.
`SLOT0_SET_IOV_LENGTH` is statically asserted to 24 bytes. Consequently
SETREGSET includes exactly the header and one complete slot; no byte belonging
to slot 1 or later is submitted.

The source also asserts every relevant member offset, not merely total struct
sizes. The NDK r27d build passed all assertions.

Result: `SETREGSET_IOV_LAYOUT_VALID = YES` and
`SLOT0_ONLY_WRITE_PROVEN = YES`.
