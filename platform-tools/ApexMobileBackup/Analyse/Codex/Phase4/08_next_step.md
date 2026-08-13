# Phase4 - Prochaine etape

## Inconnue bloquante

La prochaine inconnue prioritaire est le callback/reponse configure par:

```text
RequestAvatarServerList
  -> FUN_07a31858
  -> FUN_06bc68e8
  -> FUN_06bc6ca0 / callbacks virtuels
  -> UNKNOWN response handler
```

## Travail statique recommande

1. Decompiler `FUN_06bc6ca0` et les fonctions de la vtable `UNK_0a732320/0a732390`.
2. Identifier le callback qui emet l'evenement `0x138`.
3. Suivre ce callback jusqu'aux ecritures sur une instance `Login + 0x150`.
4. Resoudre la cible virtuelle `vtable + 0xa58` du thunk `SyncPayloadToGameServer`.
5. Seulement ensuite verifier une chaine vers la connexion GameServer.

## Critere de sortie

La chaine suivante ne deviendra CONFIRMED qu'avec des appels ou acces data explicites:

```text
RequestAvatarServerList
  -> response callback
  -> EVENTID_AVATARSERVERLIST_RETURN (0x138)
  -> GameServerBackupIpList / ServerInfo
  -> GameServer connection
```

Aucune analyse dynamique n'est recommandee avant cette resolution statique.
