# Evidence table

| Finding | Evidence | Status |
|---|---|---|
| Callback initializer direct callers | exact raw `B`/`BL` target scan | None |
| Owner initializer | no initializer caller or owner construction chain | Unknown |
| Exact `CheckUpdate` FDE | decoded `.eh_frame_hdr` and `.eh_frame` | Confirmed |
| First readable continuation | coherent bounded decode from ELF `0x05a2f000` | Confirmed |
| Owner register | field accesses use callee-saved `X19` | Confirmed; source unknown |
| Non-null invariant | immediate vtable dereference without readable null test | Established before frontier |
| `+0x1f0` stores | one alias-based `Shutdown` clear | Confirmed clear only |
| Non-null `+0x1f0` writer | none in bounded proven functions | Not recovered |
| Acquisition mechanism | no writer source | Unknown |
| `+0x1f8` lifecycle | three reads, virtual cleanup, one clear | Related interface probable |
| `W23` producer | two exact direct helpers and bit-0 selection | Confirmed source; semantics unknown |
| `+0x45` lifecycle | one zero write, two one writes, Init gate read | Persistent state flag |
| GCloud producer connection | ABI compatibility without acquisition provenance | Unknown |

Absence of evidence is reported as unknown; dead code, strings, and proximity
were not promoted.
