# Phase3 - 08 - Reconnexion

## Cibles Phase2

- `ReconnectSyncData`
- `OnServerAboutToReconnect`: `0x21f5446`
- `OnPreReconnectOnServer`: `0x221d27a`
- `ClientNotifyReconnectedSuccessfully`: `0x218a330`

## Fonctions candidates Ghidra

Source:

- `Phase3\output\ghidra_nearest_functions.json`

Candidates par intervalle lineaire d'entrees Ghidra:

| Cible code | Adresse | Fonction candidate | Delta | Prochaine fonction |
|---|---:|---|---:|---|
| `ReconnectSyncData_code_1` | `0x79c3b84` | `FUN_079c3420` | `0x764` | `FUN_079c40c4` |
| `ReconnectSyncData_code_2` | `0x79c3c44` | `FUN_079c3420` | `0x824` | `FUN_079c40c4` |
| `OnServerAboutToReconnect_code_1` | `0x7ea006c` | `FUN_07e9ffe4` | `0x88` | `FUN_07ea008c` |
| `OnServerAboutToReconnect_code_2` | `0x7ea0418` | `FUN_07ea03fc` | `0x1c` | `FUN_07ea043c` |
| `OnServerAboutToReconnect_code_3` | `0x7ea05c4` | `FUN_07ea059c` | `0x28` | `FUN_07ea05dc` |
| `OnServerAboutToReconnect_code_4` | `0x7ea0604` | `FUN_07ea05dc` | `0x28` | `FUN_07ea0694` |
| `OnServerAboutToReconnect_code_5` | `0x7ea06c4` | `FUN_07ea0694` | `0x30` | `FUN_07ea073c` |
| `OnPreReconnectOnServer_code_1` | `0x79c9308` | `FUN_079c90bc` | `0x24c` | `FUN_079c96d4` |
| `OnPreReconnectOnServer_code_2` | `0x79c9388` | `FUN_079c90bc` | `0x2cc` | `FUN_079c96d4` |
| `OnPreReconnectOnServer_code_3` | `0x79c9408` | `FUN_079c90bc` | `0x34c` | `FUN_079c96d4` |
| `ClientNotifyReconnectedSuccessfully_code_1` | `0x7a1acd8` | `FUN_07a1a7c8` | `0x510` | `FUN_07a1b780` |

## Interpretation

Ce que Phase3 ajoute:

- Les references code Phase2 tombent dans des plages lineaires proches de fonctions Ghidra `FUN_...`.
- `ReconnectSyncData` et `OnPreReconnectOnServer` se groupent autour de fonctions plus larges (`FUN_079c3420`, `FUN_079c90bc`).

Ce qui reste inconnu:

- proprietaires/classes;
- callers;
- parametres;
- lien avec `NetConnection`;
- adresse serveur courante;
- retry/reconnect destination;
- structure serveur contenant IP/port.

## IP/port ou structure serveur

Statut: UNKNOWN.

Aucune preuve Phase3 ne revele:

- `ServerIP`;
- `ServerPort`;
- `ServerAddr`;
- `Host`;
- `Port`;
- `sockaddr`;
- destination reconnect.

## Conclusion

La reconnexion contient des metadata et des fonctions candidates, mais ne revele pas encore d'IP/port ni de structure serveur. Le chemin reconnect ne confirme pas le flux server-list -> adresse game server.
