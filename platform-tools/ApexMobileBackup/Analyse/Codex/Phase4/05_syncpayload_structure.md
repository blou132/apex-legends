# Phase4 - SyncPayloadToGameServer

Chaine: `0x231f64a` (ELF `0x221f64a`). Six relocations Phase3C sont confirmees.

## Classification des slots

| Slot | Classification | Statut |
|---:|---|---|
| `0xac489d0` | entree native `[name][function]`, fonction `0xa220f70` | CONFIRMED |
| `0xac48ac8` | descripteur de parametres UFunction | PROBABLE |
| `0xac48c20` | function-info genere; constructeur `0x7d14e48` adjacent | CONFIRMED |
| `0xae87bf8` | metadata runtime avec pointeurs de fonction | PROBABLE |
| `0xae87c18` | descripteur UFunction/flags | PROBABLE |
| `0xae87c68` | metadata de classe/propriete generee | PROBABLE |

## Pointeur natif

**CONFIRMED**:

```text
0xac489d0 -> "SyncPayloadToGameServer"
0xac489d8 -> FUN_0a220f70
```

Les voisins `TryAddPendingTeamId -> FUN_07d14ab8` et `WhenTeamStateDestroyed -> FUN_07d14a3c` confirment le stride `0x10`.

`FUN_0a220f70` est un thunk tres court: il avance le frame d'appel puis invoque virtuellement `vtable + 0xa58`. La cible dynamique n'est pas resolue statiquement.

## Nature reseau

**UNKNOWN**. Le nom suggere un envoi vers le GameServer, mais aucun chemin confirme vers `ProcessRemoteFunction`, `NetDriver`, `ActorChannel` ou `SendBunch` n'est etabli. La fonction ne doit donc pas encore etre classee RPC.

Export: `output/syncpayload_structure.json`.
