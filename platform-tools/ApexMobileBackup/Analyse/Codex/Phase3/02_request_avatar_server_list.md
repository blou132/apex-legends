# Phase3 - 02 - RequestAvatarServerList

## Cibles Phase2

Phase2 avait identifie:

- `RequestAvatarServerList` dans `.rodata`: `0x21c409f`
- Metadata:
  - `0xa8db6c8`
  - `0xa8e7b70`
  - `0xa8ecb10`
- References code candidates:
  - `0x7941d2c`
  - `0x7941d6c`

## Resultat Ghidra

Export Ghidra utilise:

- `Phase3\output\ghidra_export_functions.json`
- `Phase3\output\ghidra_nearest_functions.json`

Fonctions candidates par intervalle d'entrees:

| Adresse cible | Fonction Ghidra precedente | Delta | Prochaine fonction | Conclusion |
|---:|---|---:|---|---|
| `0x7941d2c` | `FUN_07941d10` | `0x1c` | `FUN_07941e84` | candidate metadata/reflection |
| `0x7941d6c` | `FUN_07941d10` | `0x5c` | `FUN_07941e84` | candidate metadata/reflection |

Interpretation:

- `FUN_07941d10` est la seule fonction Ghidra candidate couvrant lineairement les deux references code Phase2.
- Les deux references Phase2 pointaient vers une zone metadata proche de `RequestAvatarServerList`, pas directement vers la chaine `.rodata`.
- Aucune preuve ne montre que `FUN_07941d10` est la fonction native C++ reelle appelee par l'UFUNCTION.

## Native function pointer

Recherche demandee:

```text
UFUNCTION metadata
  -> native function pointer
  -> fonction C++ reelle
```

Etat:

- Metadata UFUNCTION/reflection: CONFIRMED par Phase2 et Ghidra.
- Fonction de construction/enregistrement metadata candidate: PROBABLE `FUN_07941d10`.
- Pointeur natif associe a `RequestAvatarServerList`: UNKNOWN.
- Fonction C++ native reelle: UNKNOWN.
- Callers/callees de `FUN_07941d10`: UNKNOWN, car le script local de decompilation/xrefs n'a pas pu etre charge par Ghidra headless.

## Patterns Unreal

Termes demandes:

- `UFunction`
- `ProcessEvent`
- `NativeFunc`
- `FNativeFuncPtr`
- `exec*`
- `StaticRegisterNatives`
- `RegisterNatives`
- `FNativeFunctionRegistrar`

Resultat:

- Aucun lien Ghidra cible entre ces patterns et `RequestAvatarServerList` n'a ete exporte.
- Les noms peuvent exister ailleurs dans `libUE4.so`, mais aucun chemin prouve `RequestAvatarServerList -> NativeFunc` n'a ete obtenu pendant cette Phase3.

## Conclusion

`RequestAvatarServerList` reste rattache a de la metadata Unreal/reflection. Ghidra ajoute un candidat utile, `FUN_07941d10`, pour les deux references code Phase2, mais la fonction native C++ reelle et son pointeur d'enregistrement restent UNKNOWN.
