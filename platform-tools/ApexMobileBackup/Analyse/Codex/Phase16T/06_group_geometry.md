# Group geometry

The primary executable run begins at the left edge plus one cell:
ELF `0x0ae56608`. Its expected offset-to-top cell at `0x0ae565f8` lies just
outside the authorized window and was not inspected; `0x0ae56600` is a
compatible zero typeinfo cell. The primary address point is therefore
probable, not confirmed.

The primary run has 83 entries. Shutdown is entry 81 at relative slot offset
`+0x280`. The secondary address point `0x0ae568b0` has 11 entries and ends at
`0x0ae56900`; the next ABI boundary starts at `0x0ae56908`.
