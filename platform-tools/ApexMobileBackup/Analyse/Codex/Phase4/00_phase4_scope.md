# Phase4 - Portee

Date locale: 2026-08-13

## Objectif

Phase4 reconstruit les tables Unreal autour des metadata corrigees de Phase3C. La priorite est la chaine structurelle:

```text
RequestAvatarServerList
  -> relocation
  -> structure metadata Unreal
  -> pointeur de fonction ou wrapper
  -> native reelle
```

## Contraintes

- Analyse exclusivement statique.
- Projet Ghidra existant `ApexMobileUE4`, programme `libUE4.so`.
- Execution `-process libUE4.so -noanalysis -readOnly`.
- Aucun reimport, aucune analyse globale, aucun acces telephone et aucun appel reseau.
- Phase3C est la source d'autorite pour toutes les adresses.
- Modele CONFIRMED: `GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000`.
- `FUN_07941d10` reste INVALIDATED pour `RequestAvatarServerList`.

## Methode executee

L'exporteur `Phase3/ghidra_scripts/ApexPhase4Export.java` a ete execute en lecture seule. Il:

- valide l'image base `0x100000` et les trois slots Phase3C;
- exporte les fenetres `-0x100..+0x100` par pas de 8 octets;
- indexe les relocations et pointeurs bruts dans `.data.rel.ro`/`.data`;
- scanne les branches et materialisations d'adresses ARM64 dans `.text`;
- decompile uniquement les fonctions ciblees;
- sonde separement le callee runtime de `RequestAvatarServerList`.

Les sorties ne contiennent ni binaire, ni projet Ghidra, ni log brut.

## Limites

- Les types C++ ne sont pas symbolises; les noms de structures Unreal sont donc attribues par comparaison de layout.
- Une constante immediate seule n'identifie pas un handler.
- Un acces a un offset de classe seul n'identifie pas un reader/writer sans lien d'instance ou de classe.
