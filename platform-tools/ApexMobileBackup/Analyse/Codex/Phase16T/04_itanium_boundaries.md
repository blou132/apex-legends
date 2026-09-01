# Itanium boundaries

Seven local sequences match `[offset-to-top][typeinfo-or-zero][address point]`:

| Header (ELF) | Offset | Typeinfo | Address point |
| --- | ---: | ---: | --- |
| `0x0ae568a0` | `-0x28` | `0` | `0x0ae568b0` |
| `0x0ae56908` | `0` | `0` | `0x0ae56918` |
| `0x0ae56930` | `0` | `0` | `0x0ae56940` |
| `0x0ae56958` | `0` | `0` | `0x0ae56968` |
| `0x0ae56988` | `0` | `0` | `0x0ae56998` |
| `0x0ae569b0` | `0` | `0` | `0x0ae569c0` |
| `0x0ae569d8` | `0` | `0` | `0x0ae569e8` |

`ITANIUM_BOUNDARY_CANDIDATE_COUNT = 7`
