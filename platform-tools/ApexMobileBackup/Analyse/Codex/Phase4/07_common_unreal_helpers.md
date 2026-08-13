# Phase4 - Helpers Unreal communs

## `FUN_041b178c`

**CONFIRMED metadata constructor helper**. Appele par les wrappers `FUN_07a41d0c`, `FUN_07a41d4c`, `FUN_07a41d8c`, `FUN_07d14e48` et de nombreux autres.

Comportement observe:

- verifie un singleton destination;
- appelle les constructeurs owner/super fournis par le descripteur;
- cree l'objet reflection de fonction;
- ajoute les proprietes/parametres;
- finalise l'objet puis stocke le singleton.

Nom prudent: `ConstructUFunction-like`.

## `FUN_048e8b24`

**CONFIRMED class/struct metadata constructor helper**. Il construit un objet reflection plus grand, applique flags, nom, proprietes et fonctions, puis finalise le type. Utilise notamment par `FUN_07fb8344` et `FUN_07d14d84`.

Nom prudent: `ConstructUClassOrUStruct-like`.

## `FUN_04763790`

**PROBABLE static class bootstrap helper**. Il alloue/initialise une structure globale, enregistre constructeurs et callbacks, puis finalise le type statique.

## Distinction utile

- `FUN_041b178c`: construction `UFunction`/metadata de fonction.
- `FUN_048e8b24`: construction type/classe reflection.
- `FUN_04763790`: bootstrap statique de classe.
- `FUN_07a31858` et `FUN_0a220f70`: thunks runtime natifs, pas constructeurs metadata.

Export: `output/unreal_helpers.json`.
