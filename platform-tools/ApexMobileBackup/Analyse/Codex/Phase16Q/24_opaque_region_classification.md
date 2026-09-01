# Opaque region classification

The exact ELF prefix `0x05a2eb00..0x05a2f000` contains 146 undefined words
and 174 words that decode individually but form repeated, implausible patterns
without a reliable CFG. The following 879 words form a coherent continuation.

The evidence supports `MIXED_CODE_DATA` and partial static recovery. It does
not establish encryption, self-modification, packing, or anti-cheat behavior.

```text
CHECKUPDATE_OPAQUE_REGION_CLASS = MIXED_CODE_DATA
OPAQUE_REGION_STATIC_RECOVERABILITY = PARTIAL
```
