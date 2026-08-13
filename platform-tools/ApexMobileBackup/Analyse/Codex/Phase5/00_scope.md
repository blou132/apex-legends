# Phase5 - Perimetre

Date: 2026-08-13.

## Objectif

Cette phase part exclusivement du chemin runtime confirme en Phase4:

```text
RequestAvatarServerList
  -> FUN_07a31858
  -> FUN_06bc68e8
  -> objet callback / FUN_06bc6ca0
```

La priorite est d'identifier le callback de reponse HTTP, puis de verifier si son callgraph atteint le parsing d'une liste de serveurs, l'evenement `0x138` et une ecriture sur une instance `Login + 0x150`.

## Methode

- analyse statique dans le projet Ghidra existant;
- `-process libUE4.so -noanalysis -readOnly`;
- adresses Ghidra converties en ELF avec `ELF = GHIDRA - 0x100000`;
- exports limites a `FUN_06bc68e8`, `FUN_06bc6ca0`, aux vtables candidates et a leurs callgraphs cibles;
- profondeur maximale de cinq uniquement apres identification d'un handler de reponse;
- aucune recherche globale d'immediats `0x138` ou d'acces `+0x150`;
- aucun appel reseau, test d'URL, patch de binaire ou instrumentation dynamique.

## Regles de preuve

Les conclusions utilisent `CONFIRMED`, `PROBABLE`, `UNKNOWN` ou `INVALIDATED`. Une chaine ou un nom proche ne suffit pas a attribuer une fonction, un format de reponse, un endpoint ou un champ de structure.

## Publication

Seuls les scripts, rapports Markdown et exports JSON nettoyes sont publies. Le binaire, le projet Ghidra, les caches, les journaux bruts, les secrets et les donnees privees restent locaux conformement a `PUBLIC_DATA_POLICY.md`.
