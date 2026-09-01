# `+0x1f0` store candidates

The field-specific pass was restricted to the exact `CheckUpdate` FDE and
three already proven owner lifecycle methods. It supports direct stores and
address aliases.

One candidate is proven:

| Ghidra | ELF | Dataflow | Classification |
|---|---|---|---|
| `0x04812ee0` | `0x04712ee0` | `ADD X19,owner,#0x1f0`; `STR XZR,[X19]` | `PROVEN_CLEAR` |

No readable `CheckUpdate` instruction stores to `+0x1f0`. The opaque prefix
cannot be searched semantically because owner-register provenance does not
survive into it.

```text
OWNER_PLUS_0X1F0_STORE_CANDIDATE_COUNT = 1
OWNER_PLUS_0X1F0_STORE_CLASSIFICATIONS = ELF_0x04712ee0_PROVEN_CLEAR
OPAQUE_PRELUDE_PLUS_0X1F0_STORE = UNKNOWN
```
