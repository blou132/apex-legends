# CheckUpdate raw validity

Every aligned word in the exact 4796-byte FDE was decoded in a read-only
bounded pass.

| Classification | Words |
|---|---:|
| coherent `VALID_AARCH64` continuation | 879 |
| `UNDEFINED_ENCODING` in the prefix | 146 |
| decodable but `DATA_LIKE` prefix words without reliable CFG | 174 |
| total | 1199 |

The first `0x500` bytes, ELF `0x05a2eb00..0x05a2f000`, form a mixed opaque
prefix. The following 879 words decode coherently through the FDE end.

```text
CHECKUPDATE_RAW_INSTRUCTION_VALIDITY = MIXED_879_VALID_146_UNDEFINED_174_DATA_LIKE
CHECKUPDATE_OPAQUE_PREFIX_SIZE = 0x500_BYTES
```
