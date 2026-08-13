# Phase3C - UEDSToolkit rebased

Statut modele d'adresses: CONFIRMED.

## Cibles rebased

| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |
|---|---|---:|---:|---|---|---:|---:|---:|
| socket_http.cpp | string | `0x2235c36` | `0x2335c36` | `.rodata` | ascii:socket_http.cpp | 0 | 0 | 0 |
| DSControllerComponent.cpp | string | `0x21c0196` | `0x22c0196` | `.rodata` | ascii:DSControllerComponent.cpp | 0 | 0 | 0 |
| RegisterDSControllerComponent | string | `0x2120940` | `0x2220940` | `.rodata` | ascii:RegisterDSControllerComponent | 0 | 1 | 1 |
| /Script/UEDSToolkit | string | `0x226211a` | `0x236211a` | `.rodata` | ascii:/Script/UEDSToolkit | 0 | 1 | 1 |
| UEDSToolkit metadata | metadata | `0xa98c0b8` | `0xaa8c0b8` | `.data.rel.ro` | u64:0x2220940 -> .rodata ascii:RegisterDSControllerComponent | 0 | 40 | 1 |

## Fonctions corrigees

UNKNOWN: aucune fonction contenant les adresses corrigees n'a ete trouvee.

## Recherches texte

| Terme | Occurrences | Premieres adresses |
|---|---:|---|
| `socket_http.cpp` | 1 | 0x2335c36 |
| `DSControllerComponent.cpp` | 1 | 0x22c0196 |
| `RegisterDSControllerComponent` | 1 | 0x2220940 |
| `/Script/UEDSToolkit` | 1 | 0x236211a |
| `socket` | 4 | 0x21ddd1c, 0x2275f04, 0x22b176f, 0x22c05c9 |
| `connect` | 15 | 0x21ddd23, 0x22023db, 0x220e04c, 0x221eec5, 0x228a9c3, 0x22b20e9, 0x22b2110, 0x22e3f6b, ... |
| `send` | 4 | 0x21ddc47, 0x21e1950, 0x22b3e6f, 0xb9c4b48 |
| `recv` | 3 | 0x21ddc4c, 0x21e1946, 0xb9c4b3e |
| `getaddrinfo` | 2 | 0x21ddfee, 0x22a5096 |

JSON detaille: `output/uedstoolkit_rebased.json`.
