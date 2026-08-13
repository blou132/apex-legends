# Phase3C - LoginMgr rebased

Statut modele d'adresses: CONFIRMED.

## Cibles rebased

| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |
|---|---|---:|---:|---|---|---:|---:|---:|
| PureClient/Login/LoginMgr.cpp | string | `-` | `0x100000` | `segment_1.1` | u64:0x10102464c457f | 0 | 0 | 2 |
| ULoginMgrWrapper | string | `-` | `0x100000` | `segment_1.1` | u64:0x10102464c457f | 0 | 0 | 2 |
| LoginMgrWrapper.cpp | string | `-` | `0x100000` | `segment_1.1` | u64:0x10102464c457f | 0 | 0 | 2 |
| RequestAvatarServerList string | string | `0x21c409f` | `0x22c409f` | `.rodata` | ascii:RequestAvatarServerList | 0 | 3 | 3 |
| OpenServerList string | string | `0x226d0b2` | `0x236d0b2` | `.rodata` | ascii:OpenServerList | 0 | 1 | 1 |
| ServerListName string | string | `0x2130708` | `0x2230708` | `.rodata` | ascii:ServerListName | 0 | 1 | 1 |

## Fonctions corrigees

| Fonction | Entry | Body | Callers | Callees | Focus hits |
|---|---:|---|---:|---:|---|
| `FUN_07a41d0c` | `0x7a41d0c` | `0x7a41d0c-0x7a41d4b` | 0 | 1 |  |
| `FUN_07a41d4c` | `0x7a41d4c` | `0x7a41d4c-0x7a41d8b` | 0 | 1 |  |

## Recherches texte

| Terme | Occurrences | Premieres adresses |
|---|---:|---|
| `PureClient/Login/LoginMgr.cpp` | 1 | 0x22cab3d |
| `ULoginMgrWrapper` | 1 | 0x231434f |
| `LoginMgrWrapper.cpp` | 1 | 0x23d375d |
| `RequestAvatarServerList` | 1 | 0x22c409f |
| `OpenServerList` | 1 | 0x236d0b2 |
| `ServerListName` | 1 | 0x2230708 |

JSON detaille: `output/loginmgr_rebased.json`.
