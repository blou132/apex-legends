# Phase5 - Evenement de retour

## Chemin confirme

```text
FUN_06be413c (Ghidra 0x6be413c, ELF 0x6ae413c)
  -> FUN_06be3bdc (Ghidra 0x6be3bdc, ELF 0x6ae3bdc)
  -> FUN_06be3f4c (Ghidra 0x6be3f4c, ELF 0x6ae3f4c)
       event = 0x138 / 312
```

Ce chemin est **CONFIRMED**. A `0x6be3e90`, `w1` recoit immediatement `0x138`; le `BL` suivant a `0x6be3e94` appelle `FUN_06be3f4c` avec le booleen de resultat et le texte de reponse.

`FUN_06be3f4c` construit des noms UTF-16 lies au systeme d'evenements, puis appelle `FUN_06be427c` pour l'invocation dynamique. Cette utilisation appartient au handler de `RequestAvatarServerList` et ne provient pas d'un scan global d'immediats.

## Conditions

`FUN_06be3bdc` initialise le succes a faux et le texte a vide. Si la completion HTTP est vraie, il lit `response vtable+0x48`; seul le statut `200` place le succes a vrai et copie le corps via `response vtable+0x50`. L'evenement est ensuite emis avec ces deux valeurs si le contexte Unreal est encore valide.

Le callgraph exporte est limite a une profondeur de cinq. Les branches generiques du moteur sous `FUN_06be3f4c` ne prouvent aucune semantique supplementaire sur la liste serveur.

Export: `output/response_event.json`.
