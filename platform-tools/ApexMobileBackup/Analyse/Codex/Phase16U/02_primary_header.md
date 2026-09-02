# Primary header

The minimal authorized extension `0x0ae565c0..0x0ae56620` recovered the
missing primary header:

| Role | ELF cell | Value |
| --- | --- | --- |
| offset-to-top | `0x0ae565f8` | `0` |
| typeinfo | `0x0ae56600` | `0` |
| address point | `0x0ae56608` | first relocated executable entry |

Both the structured ELF parser and read-only Ghidra agree. The primary address
point is now `CONFIRMED`.
