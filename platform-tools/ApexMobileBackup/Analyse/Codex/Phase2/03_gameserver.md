# Phase2 - 03 - GameServer

## Portee

Fichiers lus en lecture seule:

- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libUE4.so`
- `ApexMobileBackup\Analyse\APK\classes.dex`
- artefacts `Phase2\_libUE4_reloc_xrefs.json`, `Phase2\_libUE4_code_xrefs.json`, `Phase2\_uedstoolkit_context.json`

## GameServerBackupIpList

Preuves:

- Chaine `.rodata`: `GameServerBackupIpList` a `0x2180ba5`.
- Relocations:
  - `.rela.dyn` `0x15c1498` -> `.data.rel.ro` `0xae65f20`
  - `.rela.dyn` `0x15c14c8` -> `.data.rel.ro` `0xae65f70`
- Chaines voisines dans les tables: `AuthFailedLimitMaxCount`, `AuthFailedLimitMinCount`, `AuthFailedTimeInterval`, `ChannelList`, `ChannelList_Key`.

Conclusion:

- Presence de metadata/propriete: CONFIRMED.
- Valeur d'IP, hostname, port ou tableau rempli: UNKNOWN.
- Fonction qui remplit `GameServerBackupIpList`: UNKNOWN.

## SyncPayloadToGameServer

Preuves:

- Chaine `.rodata`: `SyncPayloadToGameServer` a `0x221f64a`.
- Relocations vers `.data.rel.ro`:
  - `0xab489d0`
  - `0xab48ac8`
  - `0xab48c20`
  - `0xad87bf8`
  - `0xad87c18`
  - `0xad87c68`
- References code vers metadata:
  - `0x7c1472c`
  - `0x7c14920`
  - `0x7c14cec`
  - `0x7c14e28`
  - `0x7c14ea8`
  - `0x7eb815c`
  - `0x7eb8284`
  - `0x7eb83a4`
- Chaines/champs proches: `TeamId`, `ReturnValue`, `TryAddPendingTeamId`, `WhenTeamStateDestroyed`, `LevelCount`, `bSyncEarlyLoadLevel`, `UnitLevelSize`, `LevelOriginLocation`.

Conclusion:

- Entree UFUNCTION/RPC-like dans metadata Unreal: CONFIRMED.
- Appel concret, caller, destination reseau ou payload wire-format: UNKNOWN.
- Aucun champ `ServerIP`, `ServerPort`, `Host`, `Port`, `BackupIp` n'est relie directement a cette entree.

## UEDSToolkit et DSController

Preuves dans `libUE4.so`:

- `/Script/UEDSToolkit` a `0x226211a`.
- Occurrences `UEDSToolkit`: `0x2175b68`, `0x21c016f`, `0x21c0182`, `0x2235c0f`, `0x2235c22`, `0x2262122`.
- `DSControllerComponent.cpp` autour de `0x21c0147`.
- `RegisterDSControllerComponent` a `0x2120940`.
- Relocation pour `RegisterDSControllerComponent`: `.rela.dyn` `0xdfd1a0` -> `.data.rel.ro` `0xa98c0b8`.
- Chaines voisines: `AIWorldVolumeSet`, `GameModeTeamMgrComponent`, `SelectLegendTLogComponent`, `RegisterDSControllerComponent`, `RegisterAIComponent`, `MetaAIManager`, `AIHostControllerClass`.

Conclusion:

- UEDSToolkit est compile dans le binaire Android client: CONFIRMED.
- Utilisation seulement Dedicated Server, seulement client, ou les deux: UNKNOWN.
- Le prefixe `DS` suggere un contexte Dedicated Server, mais ce n'est pas une preuve suffisante.

## socket_http et transport

Preuves:

- `socket_http.cpp` est present comme chemin source autour de `0x2235be7`.
- `libUE4.so` contient des imports/symboles reseau generiques: `socket`, `connect`, `getaddrinfo`, `send`, `recv`, `sendto`, `recvfrom`, `inet_pton`, `inet_ntop`, `inet_addr`, `bind`, `listen`, `accept`, `socketpair`.
- `libUE4.so` contient aussi des chaines HTTP/libcurl/HTTPServer generiques.

Conclusion:

- Capacites socket/HTTP dans le client: CONFIRMED.
- Transport reel utilise par UEDSToolkit: UNKNOWN.
- Aucune xref ne relie actuellement `socket_http.cpp` a `GameServerBackupIpList`, `SyncPayloadToGameServer`, `connect`, `send` ou `recv`.

## Reconnexion

| Chaine | Offset / VA | Relocations metadata | Chaines proches | Conclusion |
|---|---:|---|---|---|
| `OnServerAboutToReconnect` | `0x21f5446` | `0xad72b80`, `0xad72c18`, `0xad72e40` | `CheckTimeLine`, `RPC_ClientSyncTimeLine`, `RPC_ServerReportInfo`, `InServerTime`, `IosTimeJumpSize` | Evenement/RPC metadata confirme; transport UNKNOWN |
| `OnPreReconnectOnServer` | `0x221d27a` | `0xa966210`, `0xa967210`, `0xa969838` | `ClientSyncLaunch`, `EndMotionMovement`, `OnRecoverOnServer`, `OnRep_ServerControllerLost` | Metadata confirme; appel UNKNOWN |
| `ClientNotifyReconnectedSuccessfully` | `0x218a330` | `0xa9ae008`, `0xa9b0698`, `0xa9ba988` | `ClientEnterStageFight`, `ClientHandleMsg`, `ClientReceiveBroadcastMsg` | Metadata confirme; appel UNKNOWN |

## Recherche structure IP/port

Des libelles de schema/reporting existent dans `classes.dex`:

- `server ip address` a `0x388eab`
- `Server IP port` a `0x3890b2`
- `network latency` a `0x388e4b`
- `protocol type` a `0x3890e2`

Ces chaines ressemblent a un schema de telemetrie/reporting. Elles ne sont pas reliees par xref aux metadata `GameServerBackupIpList`, `RequestAvatarServerList` ou `SyncPayloadToGameServer`.

## Graphe demande

```text
server-list response
  -> UNKNOWN
adresse serveur
  -> UNKNOWN
GameServerBackupIpList
  -> UNKNOWN
connexion socket
  -> UNKNOWN
SyncPayloadToGameServer
```

## Conclusion

Aucune preuve ne permet de reconstruire le flux server-list -> adresse serveur -> backup IP list -> socket -> `SyncPayloadToGameServer`. Les noms existent, les metadata UE existent, et les imports reseau existent, mais les liens runtime entre ces elements restent UNKNOWN avec les outils disponibles.
