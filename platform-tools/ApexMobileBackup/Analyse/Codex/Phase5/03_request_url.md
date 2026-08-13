# Phase5 - URL de RequestAvatarServerList

## Source

**CONFIRMED**: l'URL n'est pas construite dans `FUN_06bc68e8`.

Le thunk `FUN_07a31858` decode un `FString` depuis le frame Unreal, le copie, puis le transmet comme second argument a `FUN_06bc68e8`. Le site `0x6bc69a0` passe ce meme objet a `request vtable+0x58`, avant la configuration du verbe `GET`.

```text
appelant Unreal / Blueprint
  -> FString URL
  -> FUN_07a31858
  -> FUN_06bc68e8
  -> request vtable+0x58(URL)
```

## Valeur finale

**UNKNOWN**: aucun composant `http://`, `https://`, hote, chemin, region ou port n'est present dans le callgraph natif cible. La valeur est fournie au runtime par l'appelant dynamique de la UFunction.

La fonction ne concatene pas de base URL et n'ajoute pas d'en-tete visible. Aucune URL n'a ete contactee ou testee.

Preuve machine: `output/request_build.json`.
