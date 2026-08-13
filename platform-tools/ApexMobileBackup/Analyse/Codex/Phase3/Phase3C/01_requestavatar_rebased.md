# Phase3C - RequestAvatarServerList rebased

Statut modele d'adresses: CONFIRMED.

## Cibles rebased

| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |
|---|---|---:|---:|---|---|---:|---:|---:|
| RequestAvatarServerList string | string | `0x21c409f` | `0x22c409f` | `.rodata` | ascii:RequestAvatarServerList | 0 | 3 | 3 |
| RequestAvatar metadata 1 | metadata | `0xa8db6c8` | `0xa9db6c8` | `.data.rel.ro` | u64:0x22c409f -> .rodata ascii:RequestAvatarServerList | 0 | 80 | 0 |
| RequestAvatar metadata 2 | metadata | `0xa8e7b70` | `0xa9e7b70` | `.data.rel.ro` | u64:0x22c409f -> .rodata ascii:RequestAvatarServerList | 0 | 45 | 0 |
| RequestAvatar metadata 3 | metadata | `0xa8ecb10` | `0xa9ecb10` | `.data.rel.ro` | u64:0x22c409f -> .rodata ascii:RequestAvatarServerList | 0 | 80 | 0 |
| RequestAvatar code 1 | code | `0x7941d2c` | `0x7a41d2c` | `.text` | instruction:adrp x1,0xa9e7000 | 0 | 0 | 0 |
| RequestAvatar code 2 | code | `0x7941d6c` | `0x7a41d6c` | `.text` | instruction:adrp x1,0xa9e7000 | 0 | 0 | 0 |

## Comparaison ancienne fonction

- Ancienne `FUN_07941d10`: `FUN_07941d10` @ `0x7941d10` body `0x7941d10-0x7941e83`.
- Fonction contenant `0x7a41d2c`: `FUN_07a41d0c` @ `0x7a41d0c` body `0x7a41d0c-0x7a41d4b`.
- Fonction contenant `0x7a41d6c`: `FUN_07a41d4c` @ `0x7a41d4c` body `0x7a41d4c-0x7a41d8b`.
- EST-CE LA MEME FONCTION ? NO.
- INVALIDATED: l'ancienne conclusion liant directement `FUN_07941d10` a `RequestAvatarServerList` ne doit plus etre conservee.

## Fonctions corrigees

| Fonction | Entry | Body | Callers | Callees | Focus hits |
|---|---:|---|---:|---:|---|
| `FUN_07941d10` | `0x7941d10` | `0x7941d10-0x7941e83` | 0 | 3 |  |
| `FUN_07a41d0c` | `0x7a41d0c` | `0x7a41d0c-0x7a41d4b` | 0 | 1 |  |
| `FUN_07a41d4c` | `0x7a41d4c` | `0x7a41d4c-0x7a41d8b` | 0 | 1 |  |

## Recherches texte

| Terme | Occurrences | Premieres adresses |
|---|---:|---|
| `RequestAvatarServerList` | 1 | 0x22c409f |
| `FNativeFunctionRegistrar` | 0 | - |
| `RegisterFunction` | 0 | - |
| `NativeFunc` | 0 | - |
| `ProcessEvent` | 0 | - |
| `UFunction` | 0 | - |
| `StaticRegisterNatives` | 0 | - |
| `execRequestAvatarServerList` | 0 | - |

JSON detaille: `output/requestavatar_rebased.json`.
