# Cross-library import audit

Targeted ELF metadata inspection covered the exact preserved native set. Only
`libUE4.so` has an undefined global `CreateDolphin` symbol and a corresponding
PLT relocation.

| Module | Imports `CreateDolphin` | Relocation | Evidence |
|---|---:|---|---|
| `libUE4.so` | Yes | `R_AARCH64_JUMP_SLOT` | dynsym index 375; ELF relocation `0x0b37e8c8` |
| `libgcloud.so` | Provider | N/A | global dynamic export at ELF `0x0044514c` |
| Other exact APK native libraries | No | None | no undefined symbol or exact relocation |

Because the exact static import is proven, an exact-string `dlsym` expansion
was neither required nor justified.

```text
CREATEDOLPHIN_DYNAMIC_LOOKUP = NO
DYNAMIC_LOOKUP_OWNER_MODULE = NOT_REQUIRED
```
