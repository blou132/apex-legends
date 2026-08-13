# Phase3C - GameServerBackupIpList rebased

Statut modele d'adresses: CONFIRMED.

## Cibles rebased

| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |
|---|---|---:|---:|---|---|---:|---:|---:|
| GameServerBackupIpList string | string | `0x2180ba5` | `0x2280ba5` | `.rodata` | ascii:GameServerBackupIpList | 0 | 2 | 2 |
| GameServerBackup metadata 1 | metadata | `0xae65f20` | `0xaf65f20` | `.data.rel.ro` | u64:0x2280ba5 -> .rodata ascii:GameServerBackupIpList | 0 | 41 | 1 |
| GameServerBackup metadata 2 | metadata | `0xae65f70` | `0xaf65f70` | `.data.rel.ro` | u64:0x2280ba5 -> .rodata ascii:GameServerBackupIpList | 0 | 48 | 1 |

## Comparaison metadata Phase3B

Les anciennes zones `0xae65f20` / `0xae65f70` et les zones rebased `0xaf65f20` / `0xaf65f70` sont comparees dans le JSON. Si les pointeurs et chaines different, l'attribution Phase3B reste INVALIDATED.

## Fonctions corrigees

UNKNOWN: aucune fonction contenant les adresses corrigees n'a ete trouvee.

## Recherches texte

| Terme | Occurrences | Premieres adresses |
|---|---:|---|
| `GameServerBackupIpList` | 1 | 0x2280ba5 |
| `ArrayProperty` | 1 | 0x222f572 |
| `StrProperty` | 1 | 0x23c4b70 |
| `NameProperty` | 2 | 0x22dc73e, 0x236d42b |
| `StructProperty` | 2 | 0x2291991, 0x237b3e8 |
| `MapProperty` | 1 | 0x23d3a85 |
| `SetProperty` | 1 | 0x2262ba2 |
| `ObjectProperty` | 4 | 0x2280e6b, 0x22db935, 0x23b5a0b, 0x23c4b61 |

JSON detaille: `output/gameserverbackup_rebased.json`.
