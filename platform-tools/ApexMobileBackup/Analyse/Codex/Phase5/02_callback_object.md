# Phase5 - Objet callback

## Role de FUN_06bc6ca0

`FUN_06bc6ca0` (Ghidra `0x6bc6ca0`, ELF `0x6ac6ca0`) est **CONFIRMED** comme fonction de copie/clone du delegate callback, et non comme handler HTTP.

Elle:

1. libere l'ancien contenu du stockage de destination si necessaire;
2. alloue `0x30` octets;
3. copie les champs `+0x08`, `+0x10`, `+0x18`, `+0x20` et `+0x28`;
4. incremente le compteur du pointeur partage a `+0x18`;
5. pose temporairement le vptr `0xa732390`, puis le vptr actif `0xa732320`.

## Vtables validees

Les deux adresses sont directement ecrites par `FUN_06bc68e8` et `FUN_06bc6ca0`; leur appartenance au cycle de vie du callback est donc **CONFIRMED**.

| Adresse point | ELF | Role |
|---:|---:|---|
| `0xa732320` | `0xa632320` | vtable active du callback derive |
| `0xa732390` | `0xa632390` | vtable de base/temporaire de construction/destruction |

Le dump `-0x20..+0x200` montre plusieurs vtables consecutives. Pour `0xa732320`, les methodes propres utiles se terminent a `+0x58`; la sequence qui recommence a `+0x70` est la vtable suivante et ne doit pas etre attribuee au meme objet.

## Methodes utiles de 0xa732320

| Offset | Fonction Ghidra / ELF | Role |
|---:|---|---|
| `+0x38` | `FUN_0a07f398` / `0x9f7f398` | destruction du contenu partage |
| `+0x40` | `FUN_06be3b30` / `0x6ae3b30` | destructeur avec liberation de l'objet |
| `+0x48` | `FUN_06bc6ca0` / `0x6ac6ca0` | clone/copie du delegate |
| `+0x50` | `FUN_06be3bdc` / `0x6ae3bdc` | corps du handler de completion |
| `+0x58` | `FUN_06be413c` / `0x6ae413c` | adaptateur `ExecuteIfSafe` vers le handler |

`FUN_06be413c` copie les deux pointeurs partages de requete/reponse, normalise le booleen de completion, appelle `FUN_06be3bdc` et retourne vrai. Le lien explicite est:

```text
callback vtable 0xa732320 +0x58
  -> FUN_06be413c
  -> FUN_06be3bdc
```

Export: `output/callback_vtables.json`.
