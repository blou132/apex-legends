# Phase3C - Native registration rebased

Statut modele d'adresses: CONFIRMED.

## Cibles rebased

| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |
|---|---|---:|---:|---|---|---:|---:|---:|
| RequestAvatarServerList string | string | `0x21c409f` | `0x22c409f` | `.rodata` | ascii:RequestAvatarServerList | 0 | 3 | 3 |
| SyncPayloadToGameServer string | string | `0x221f64a` | `0x231f64a` | `.rodata` | ascii:SyncPayloadToGameServer | 0 | 6 | 6 |
| OpenServerList string | string | `0x226d0b2` | `0x236d0b2` | `.rodata` | ascii:OpenServerList | 0 | 1 | 1 |

## Fonctions corrigees

| Fonction | Entry | Body | Callers | Callees | Focus hits |
|---|---:|---|---:|---:|---|
| `FUN_07a41d0c` | `0x7a41d0c` | `0x7a41d0c-0x7a41d4b` | 0 | 1 |  |
| `FUN_07a41d4c` | `0x7a41d4c` | `0x7a41d4c-0x7a41d8b` | 0 | 1 |  |
| `FUN_07d1470c` | `0x7d1470c` | `0x7d1470c-0x7d1474b` | 0 | 1 |  |
| `FUN_07d14900` | `0x7d14900` | `0x7d14900-0x7d1493f` | 0 | 1 |  |
| `FUN_07d14cc8` | `0x7d14cc8` | `0x7d14cc8-0x7d14d83` | 0 | 1 |  |
| `FUN_07d14e08` | `0x7d14e08` | `0x7d14e08-0x7d14e47` | 0 | 1 |  |
| `FUN_07d14e88` | `0x7d14e88` | `0x7d14e88-0x7d14ec7` | 0 | 1 |  |

## Recherches texte

| Terme | Occurrences | Premieres adresses |
|---|---:|---|
| `FNativeFunctionRegistrar` | 0 | - |
| `RegisterFunction` | 0 | - |
| `StaticRegisterNatives` | 0 | - |
| `NativeFunc` | 0 | - |
| `execRequestAvatarServerList` | 0 | - |
| `execSyncPayloadToGameServer` | 0 | - |
| `UFunction` | 0 | - |

JSON detaille: `output/native_registration_rebased.json`.
