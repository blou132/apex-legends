# Phase5 - Prochaine etape

## Inconnue bloquante

Le callback natif ne parse pas la reponse. La prochaine inconnue prioritaire est donc le consommateur de l'evenement `0x138`:

```text
FUN_06be3bdc
  -> EVENTID_AVATARSERVERLIST_RETURN (0x138)
  -> consommateur Lua/Blueprint/event UNKNOWN
  -> parsing et stockage UNKNOWN
```

## Travail statique recommande

1. Resoudre la cible dynamique construite par `FUN_06be3f4c` / `FUN_06be427c` et inventorier les consommateurs de `0x138`.
2. Chercher dans les assets script/Lua publics locaux le nom qualifie de l'evenement, sans executer le client ni contacter un service.
3. Depuis ce consommateur seulement, identifier le format du `FString response_body` et les champs lus.
4. Prouver ensuite un objet `Login` avant toute recherche d'ecriture `+0x150`.
5. Remonter l'appelant dynamique de la UFunction pour retrouver le `FString URL` fourni a `RequestAvatarServerList`.
6. Traiter separement la classe concrete de `SyncPayloadToGameServer` avant de qualifier son dispatch de RPC.

## Critere suivant

La prochaine phase doit relier explicitement:

```text
event 0x138
  -> parser identifie
  -> champs de liste serveur
  -> stockage type et owner prouves
```

Une adresse de serveur ne doit etre publiee que si le client la construit ou la lit explicitement; elle ne doit jamais etre contactee.
