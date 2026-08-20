# Dialog string anchors

The exact Phase15L title and three minimal distinctive message fragments are
present as UTF-16LE strings in `.rodata`.

| Anchor | File offset | ELF VA | Ghidra | Section |
| --- | ---: | ---: | ---: | --- |
| `Unable to run on this device!` | `0x274437e` | `0x274437e` | `0x284437e` | `.rodata` |
| `Device has OpenGL ES 3.1 support` | `0x25222ae` | `0x25222ae` | `0x26222ae` | `.rodata` |
| `Floating point render target support` | `0x25830aa` | `0x25830aa` | `0x26830aa` | `.rodata` |
| `the app was not packaged with ES2 support` | `0x246496a` | `0x246496a` | `0x256496a` | `.rodata` |

The full proprietary message is not reproduced here. The title is materialized
exactly by `ADRP x2, 0x2844000; ADD x2, x2, #0x37e` at Ghidra
`0x59ef9b0-0x59ef9b4` inside `FUN_059ef114`.
