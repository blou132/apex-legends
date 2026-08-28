# PotatoNV integrity

The official upstream Phase16D archive and its extracted executable were
rehashed in place. Neither file was executed.

| Artifact | SHA256 | Phase16D match |
| --- | --- | --- |
| `PotatoNV-next-v2.2.1_2022.03-x86.zip` | `98344A77EEDDEE99F4CA145C586A6656B7F98DA0CC04BE1007DC102EC62AE416` | YES |
| `PotatoNV-next.exe` | `2C1964BF2E4F8774E1688D01D140EF2BAD3CAF1077E17C6F7C27400F3C7707A7` | YES |

The executable remains Authenticode `NOT_SIGNED`, as already documented in
Phase16D. Integrity trust therefore comes from the pinned official upstream
release archive and repeated hashes, not a Windows publisher signature.

```text
POTATONV_RELEASE = 2022.03 / v2.2.1
POTATONV_BINARY_HASH_MATCH = YES
POTATONV_EXECUTED = NO
```
