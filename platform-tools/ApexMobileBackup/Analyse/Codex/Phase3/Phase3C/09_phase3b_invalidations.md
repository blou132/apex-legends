# Phase3C - Invalidations Phase3B

Regle appliquee: une conclusion fondee sur une adresse Phase2 non rebased est marquee INVALIDATED si la fonction ou la zone Ghidra corrigee differe.

| Cible | Ancienne Phase2 | Ancienne Phase3B | Ghidra reelle | Delta | Ancienne fonction | Nouvelle fonction | Ancienne conclusion valide ? |
|---|---:|---:|---:|---:|---|---|:---:|
| RequestAvatarServerList | `0x21c409f` | `0x21c409f` | `0x22c409f` | `0x100000` | `FUN_07941d10 0x7941d10` | `FUN_07a41d0c 0x7a41d0c` | NO |
| EVENTID_AVATARSERVERLIST_RETURN | `0x217dbf5` | `0x217dbf5` | `0x227dbf5` | `0x100000` | `-` | `-` | NO |
| OpenServerList | `0x226d0b2` | `0x226d0b2` | `0x236d0b2` | `0x100000` | `-` | `-` | NO |
| ServerListName | `0x2130708` | `0x2130708` | `0x2230708` | `0x100000` | `-` | `-` | NO |
| GameServerBackupIpList | `0x2180ba5` | `0x2180ba5` | `0x2280ba5` | `0x100000` | `-` | `-` | NO |
| SyncPayloadToGameServer | `0x221f64a` | `0x221f64a` | `0x231f64a` | `0x100000` | `FUN_07c14710 0x7c14710` | `FUN_07d1470c 0x7d1470c` | NO |
| OnServerAboutToReconnect | `0x21f5446` | `0x22f5446` | `0x22f5446` | `0x100000` | `-` | `-` | YES |
| OnPreReconnectOnServer | `0x221d27a` | `0x231d27a` | `0x231d27a` | `0x100000` | `-` | `-` | YES |
| ClientNotifyReconnectedSuccessfully | `0x218a330` | `0x228a330` | `0x228a330` | `0x100000` | `-` | `-` | YES |
| RegisterDSControllerComponent | `0x2120940` | `0x2220940` | `0x2220940` | `0x100000` | `-` | `-` | YES |
| /Script/UEDSToolkit | `0x226211a` | `0x226211a` | `0x236211a` | `0x100000` | `-` | `-` | NO |

Les details par cible sont dans les fichiers `01_...` a `08_...` et dans les JSON `output/*_rebased.json`.
