# Negative offset boundary

ELF `0x0ae568a0` holds signed `-0x28`; the following cell at
`0x0ae568a8` is zero; and `0x0ae568b0` relocates to executable code. This is
the exact Itanium secondary-vtable shape. The first secondary entry adjusts
`x0` by `-0x28`, independently confirming the header value's ABI meaning.

`NEGATIVE_0X28_IS_OFFSET_TO_TOP_COMPATIBLE = YES`

`ZERO_CELL_IS_TYPEINFO_POSITION_COMPATIBLE = YES`
