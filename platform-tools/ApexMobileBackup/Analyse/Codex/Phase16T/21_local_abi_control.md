# Local ABI control

The immediately following unrelated group begins at ELF `0x0ae56908` with
zero offset-to-top, zero typeinfo, and address point `0x0ae56918`. It contains
three executable entries before the next zero/zero boundary at `0x0ae56930`.
Five additional zero/zero boundaries repeat the same local layout.

This control validates the boundary rule and shows that zero typeinfo cells
are common in the neighborhood.

`ITANIUM_LOCAL_CONTROL_VALID = YES`

`ZERO_TYPEINFO_COMMON_LOCALLY = YES`
