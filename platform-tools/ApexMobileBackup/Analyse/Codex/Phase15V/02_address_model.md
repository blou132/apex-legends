# libgcloud address model

The address rule was derived for `libgcloud.so`; the libUE4 rule was not
blindly reused.

The ELF64 little-endian program headers contain these load segments:

| Segment | File offset | ELF VA | File size | Memory size |
| --- | ---: | ---: | ---: | ---: |
| PT_LOAD 0 | `0x0` | `0x0` | `0x84d644` | `0x84d644` |
| PT_LOAD 1 | `0x84e050` | `0x85e050` | `0x51008` | `0x70f00` |

Ghidra reports image base `0x100000`. The minimum ELF load VA is `0x0`.
Therefore, for this imported program:

```text
GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000
ELF_VIRTUAL_ADDRESS = GHIDRA_ADDRESS - 0x100000
```

Executable addresses in the first segment also have file offset equal to ELF
VA. Data in the second segment must use its segment mapping. For example,
Ghidra data reference `0x977c80` maps to ELF VA `0x877c80` and file offset
`0x867c80`.

No runtime load address or load bias is inferred here.
