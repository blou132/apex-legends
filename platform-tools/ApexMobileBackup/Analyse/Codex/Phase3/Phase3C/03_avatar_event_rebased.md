# Phase3C - EVENTID_AVATARSERVERLIST_RETURN rebased

Statut modele d'adresses: CONFIRMED.

## Cibles rebased

| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |
|---|---|---:|---:|---|---|---:|---:|---:|
| EVENTID_AVATARSERVERLIST_RETURN string | string | `0x217dbf5` | `0x227dbf5` | `.rodata` | ascii:EVENTID_AVATARSERVERLIST_RETURN | 0 | 0 | 0 |

## Fonctions corrigees

UNKNOWN: aucune fonction contenant les adresses corrigees n'a ete trouvee.

## Recherches texte

| Terme | Occurrences | Premieres adresses |
|---|---:|---|
| `EVENTID_AVATARSERVERLIST_RETURN` | 1 | 0x227dbf5 |
| `ELuaCppEventType::` | 160 | 0x22052e4, 0x220530b, 0x2205330, 0x2205359, 0x2205389, 0x22053b7, 0x22053e7, 0x220540e, ... |
| `LuaCppEvent` | 20 | 0x22052e5, 0x220530c, 0x2205331, 0x220535a, 0x220538a, 0x22053b8, 0x22053e8, 0x220540f, ... |
| `DispatchEvent` | 1 | 0x21dd417 |
| `SendEvent` | 1 | 0x22c5a79 |
| `TriggerEvent` | 5 | 0x220e2a6, 0x223be65, 0x225df98, 0x22ed5d0, 0x233095b |

JSON detaille: `output/avatar_event_rebased.json`.
