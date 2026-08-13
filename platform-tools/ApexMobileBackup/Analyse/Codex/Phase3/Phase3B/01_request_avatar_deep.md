# Phase3B - RequestAvatarServerList deep dive

## Sources

- `Phase3\Phase3B\output\request_avatar_xrefs.json`
- `Phase3\Phase3B\output\request_avatar_metadata.json`

## Resultat

UNKNOWN: la fonction native exacte de `RequestAvatarServerList` n'est pas confirmee.

PROBABLE: `FUN_07941d10` est un helper de metadata/reflection lie au contexte repere en Phase2, mais ce n'est pas une preuve suffisante pour l'identifier comme implementation native de `RequestAvatarServerList`.

## Elements confirmes

- Programme: `libUE4.so`, `AARCH64:LE:64:v8A`, image base `0x100000`.
- Cible Phase2 `RequestAvatarServerList_string`: `0x21c409f`, bloc `.rela.dyn`, aucune xref.
- Occurrence brute de la chaine: `0x22c409f`, preview `RequestAvatarServerList`, aucune xref.
- Metadata candidates:
  - `0xa8db6c8`, bloc `.data.rel.ro`, aucune xref;
  - `0xa8e7b70`, bloc `.data.rel.ro`, aucune xref;
  - `0xa8ecb10`, bloc `.data.rel.ro`, aucune xref.
- Adresses code candidates:
  - `0x7941d2c` dans `FUN_07941d10`, aucune xref entrante, 1 xref sortante locale `CONDITIONAL_JUMP` vers `0x7941da8`;
  - `0x7941d6c` dans `FUN_07941d10`, aucune xref entrante ou sortante directe.

## Fonction candidate

`FUN_07941d10 @ 0x7941d10`:

- callers directs exportes: 0;
- callees directs: `FUN_04a5f384`, `FUN_0492915c`, `FUN_04ef6994`;
- decompilation reussie pour la racine.

Extrait limite de decompilation:

```c
void FUN_07941d10(long param_1,long param_2)
{
  uint *puVar1;
  uint *puVar2;
  ...
  puVar1 = *(uint **)(param_2 + 8);
  for (puVar2 = *(uint **)(*(uint **)(param_2 + 8) + 2); puVar2 != (uint *)0x0;
      puVar2 = *(uint **)(puVar2 + 2)) {
    puVar1 = puVar2;
  }
  if ((*puVar1 & 1) == 0) {
    *puVar1 = *puVar1 | 1;
    if (*(long *)(*(long *)(puVar1 + 4) + 0x38) == 0) {
      plVar6 = *(long **)(*(long *)(puVar1 + 4) + 0x148);
```

Ce comportement ressemble a une routine de parcours / initialisation d'objet ou de metadata. L'export ne montre pas d'appel ou de reference textuelle a `RequestAvatarServerList` dans ce code.

## Fenetres metadata

Les fenetres autour des trois adresses metadata contiennent surtout des pointeurs vers `.text`.

Exemples:

- `0xa8db6c8` pointe au centre vers une zone de fonctions `FUN_0a3a18d4`, `FUN_0a3a18dc` et voisines.
- `0xa8e7b70` pointe vers `FUN_08395de4` et voisines.
- `0xa8ecb10` pointe vers `FUN_0a000b4c` et une table avec fonctions `FUN_0a146...`; `__cxa_pure_virtual` apparait dans la zone voisine.

Aucun slot n'est confirme comme pointeur `NativeFunc`.

## Conclusion

La chaine `RequestAvatarServerList` est presente dans le binaire, mais elle n'a pas de xref Ghidra exploitable. Les adresses metadata et code de Phase2 restent des candidats structurels. La native exacte, son pointeur et ses callers restent UNKNOWN.
