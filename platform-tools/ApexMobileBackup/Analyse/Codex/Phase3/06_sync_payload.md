# Phase3 - 06 - SyncPayloadToGameServer

## Cibles Phase2

- Chaine `.rodata`: `SyncPayloadToGameServer` a `0x221f64a`
- Metadata:
  - `0xab489d0`
  - `0xab48ac8`
  - `0xab48c20`
  - `0xad87bf8`
  - `0xad87c18`
  - `0xad87c68`
- Code candidates:
  - `0x7c1472c`
  - `0x7c14920`
  - `0x7c14cec`
  - `0x7c14e28`
  - `0x7c14ea8`
  - `0x7eb815c`
  - `0x7eb8284`
  - `0x7eb83a4`

## Fonctions candidates Ghidra

Source:

- `Phase3\output\ghidra_nearest_functions.json`

Ces fonctions sont candidates par intervalle lineaire d'entrees Ghidra exportees:

| Adresse | Fonction candidate | Delta | Prochaine fonction |
|---:|---|---:|---|
| `0x7c1472c` | `FUN_07c14710` | `0x1c` | `FUN_07c14740` |
| `0x7c14920` | `FUN_07c148b0` | `0x70` | `FUN_07c14928` |
| `0x7c14cec` | `FUN_07c14cac` | `0x40` | `FUN_07c14e44` |
| `0x7c14e28` | `FUN_07c14cac` | `0x17c` | `FUN_07c14e44` |
| `0x7c14ea8` | `FUN_07c14e44` | `0x64` | `FUN_07c14f60` |
| `0x7eb815c` | `FUN_07eb80dc` | `0x80` | `FUN_07eb8224` |
| `0x7eb8284` | `FUN_07eb8224` | `0x60` | `FUN_07eb8364` |
| `0x7eb83a4` | `FUN_07eb8364` | `0x40` | `FUN_07eb862c` |

## Interpretation

Ce que Phase3 ajoute:

- Ghidra a cree des fonctions proches des adresses candidates.
- Plusieurs adresses se groupent autour de petites fonctions `FUN_07c14...` et `FUN_07eb8...`.

Ce qui n'est pas prouve:

- La fonction native exacte associee a `SyncPayloadToGameServer`.
- Les callers/callees.
- Les parametres reels.
- La classe proprietaire.
- Le type RPC exact.

## RPC Unreal ou transport custom

Hypotheses demandees:

| Hypothese | Statut | Raison |
|---|---|---|
| RPC Unreal standard | PROBABLE | nom `SyncPayloadToGameServer`, metadata Unreal/reflection, champs proches `TeamId`, `LevelCount`, `bSyncEarlyLoadLevel` |
| Appel UEDSToolkit | UNKNOWN | aucun lien xref exporte |
| HTTP/socket custom | UNKNOWN | aucun lien vers `socket`, `connect`, `send`, `recv`, `curl` exporte |
| autre | UNKNOWN | pas assez de pseudo-code |

Important: `PROBABLE` ne signifie pas que le native implementation a ete trouve. Cela signifie seulement que les metadata ressemblent a un chemin RPC Unreal.

## Conclusion

`SyncPayloadToGameServer` est toujours mieux explique par une entree metadata/UFUNCTION/RPC-like que par un endpoint socket direct. Ghidra fournit des fonctions candidates autour des references metadata, mais pas la fonction native ni les callers/callees.
