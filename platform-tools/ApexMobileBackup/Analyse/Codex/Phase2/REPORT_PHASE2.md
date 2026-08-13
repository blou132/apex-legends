# Phase2 - Rapport de synthese

## Portee

Tous les travaux nouveaux sont dans `ApexMobileBackup\Analyse\Codex\Phase2\`. Les fichiers originaux APK/OBB/PAK/SO/DEX/logs ont ete lus en lecture seule. Aucun outil n'a ete telecharge ou installe.

Outils trouves:

- `py.exe`
- `rg.exe`

Outils non trouves:

- `ghidra`, `analyzeHeadless`, `jadx`, `apktool`, `radare2`, `r2`, `rizin`, `rz-bin`, `objdump`, `llvm-objdump`, `readelf`, `strings`

## Reponses prioritaires

### 1. Quelle fonction appelle RequestAvatarServerList ?

UNKNOWN.

Preuves:

- `libUE4.so`: `RequestAvatarServerList` a `0x21c409f` dans `.rodata`.
- Relocations vers metadata `.data.rel.ro`: `0xa8db6c8`, `0xa8e7b70`, `0xa8ecb10`.
- References code detectees seulement vers metadata: `0x7941d2c` et `0x7941d6c`.

Aucun caller nomme ou corps de fonction runtime n'a ete identifie.

### 2. Quelle fonction traite EVENTID_AVATARSERVERLIST_RETURN ?

UNKNOWN.

Preuves:

- `libUE4.so`: `EVENTID_AVATARSERVERLIST_RETURN` a `0x217dbf5` dans `.rodata`.
- Contexte proche: enum-like `ELuaCppEventType::EVENTID_AVATARSERVERLIST_RETURN`.
- Aucune relocation directe ou xref code detectee.

### 3. Une structure de serveur avec IP/port a-t-elle ete identifiee ?

UNKNOWN.

Preuves:

- `GameServerBackupIpList` est present comme metadata a `0x2180ba5`, relocalise vers `0xae65f20` et `0xae65f70`.
- Aucun champ concret `ServerIP`, `ServerPort`, `ServerAddr`, `ServerAddress`, `Host`, `Port` n'est relie par xref a cette metadata.
- `classes.dex` contient des libelles de reporting (`server ip address` a `0x388eab`, `Server IP port` a `0x3890b2`), mais ils ne sont pas relies au flux server-list/game-server.

### 4. Ou GameServerBackupIpList est-elle remplie ?

UNKNOWN.

Preuves:

- `GameServerBackupIpList` a `0x2180ba5`.
- Relocations metadata: `0xae65f20`, `0xae65f70`.
- Contexte proche: `AuthFailedLimitMaxCount`, `AuthFailedLimitMinCount`, `AuthFailedTimeInterval`, `ChannelList`, `ChannelList_Key`.

Aucun site d'ecriture/remplissage n'a ete identifie.

### 5. Comment SyncPayloadToGameServer est appelee ?

UNKNOWN.

Preuves:

- `SyncPayloadToGameServer` a `0x221f64a`.
- Relocations metadata: `0xab489d0`, `0xab48ac8`, `0xab48c20`, `0xad87bf8`, `0xad87c18`, `0xad87c68`.
- Champs proches: `TeamId`, `ReturnValue`, `LevelCount`, `bSyncEarlyLoadLevel`, `UnitLevelSize`, `LevelOriginLocation`.
- References code detectees vers metadata: `0x7c1472c`, `0x7c14920`, `0x7c14cec`, `0x7c14e28`, `0x7c14ea8`, `0x7eb815c`, `0x7eb8284`, `0x7eb83a4`.

Aucune destination reseau, aucun caller concret, et aucun format payload n'ont ete confirmes.

### 6. Quel transport UEDSToolkit utilise-t-il reellement ?

UNKNOWN.

Preuves:

- UEDSToolkit est present dans le client: `/Script/UEDSToolkit` a `0x226211a`, occurrences `UEDSToolkit` a `0x2175b68`, `0x21c016f`, `0x21c0182`, `0x2235c0f`, `0x2235c22`, `0x2262122`.
- `DSControllerComponent.cpp` autour de `0x21c0147`.
- `socket_http.cpp` autour de `0x2235be7`.
- `libUE4.so` importe/contient des symboles reseau generiques (`socket`, `connect`, `getaddrinfo`, `send`, `recv`, `sendto`, `recvfrom`, `inet_pton`, etc.).

Aucune xref ne relie ces imports au module UEDSToolkit ou a un flux game-server.

### 7. A quoi servent les ports 8085/8080 de RpcAddressSvr ?

PROBABLE: ports du service d'adresse RPC GCloud. Hostname associe: UNKNOWN.

Preuves:

- `GCloud_2026081217.log:4`: `RpcAddressSvrPortList[8085|8080]`, avec `RpcAddressSvrBkIPList[]`.
- `libgcloud.so`: `RpcAddressSvrPortList` autour de `0x65736c`, `0x691542`, `0x691628`; `8085|8080` autour de `0x691558`.
- Contexte: `RpcConnectTimeout`, `RpcConnectTdrProto`, `RpcParallelChannels`, `RpcAddressTdrPortList`, `7618|443`, `GCloudRemoteConfig.mm`.

Ce service n'est pas classe GameServer.

### 8. A quoi sert /tdm/v1/route ?

PROBABLE/CONFIRMED: route de configuration/routage TDataMaster pour telemetrie/reporting, pas preuve d'un mode Team Deathmatch.

Preuves:

- `resources.arsc`: `https://tdm.mgapex.com:8013/tdm/v1/route`, offset `0x7af2`, resource ID `0x7f0e00b6`, nom `router_address_formal`.
- `classes.dex`: `R$string.router_address_formal = 0x7f0e00b6` dans `com.ea...` et `com.gcore.tmgp.gcloudcore`.
- `TDMUtils.SaveConfigInfo`, `code_off 0x85398c`, reference des cles `GCloud.TDM.*` dont `TGEMIT_ROUTER_ADDRESS_FORMAL`.
- `libTDataMaster.so`: `TDMReportEvent`, `TDMReportLogin`, `tdm_set_router_address`, `tdm_report_event`, `tdm_upload_file`; HTTP `POST`, `GET`, `application/json`, `multipart/form-data`.

Methode exacte par endpoint et schema de reponse: UNKNOWN.

### 9. ClientLaunch.lua ou sgaf_config.json sont-ils extractibles ?

UNKNOWN avec la methode actuelle.

Preuves:

- `launch.pak`: `Client/Launch/ClientLaunch.lua` a `0x250517`; marqueur `zlib_789c` proche a `0x2513f8`; pas de contenu Lua lisible dans la fenetre.
- `launch.pak`: `sgaf_config.json` a `0x37f731f` et `0x38067eb`; marqueurs zlib proches; pas de JSON lisible dans les fenetres.
- Entropie elevee autour des occurrences et absence de copie non compressee evidente.

Sans index PAK/decompression contexte/cle eventuelle, le contenu n'est pas recupere.

### 10. Existe-t-il une preuve d'une cle PAK embarquee ?

UNKNOWN.

Preuves:

- Recherche ciblee dans `libUE4.so`, autres `lib*.so`, DEX et `resources.arsc`.
- Pas de preuve PAK-specifique pour `PakEncryptionKey`, `FPakPlatformFile`, `FPakFile`, `FAES`, `RegisterEncryptionKey`, `EncryptionKeyGuid`, `EncryptedIndex`, `bEncryptedIndex`.
- Les occurrences `AES` et `Decrypt` observees sont liees a TLS/OpenSSL/GCloud ou crypto generique.

`AES_KEY_CANDIDATE_FOUND = NO` pour une preuve PAK-specifique.

### 11. Quel est le prochain obstacle technique ?

CONFIRMED: absence d'outil de desassemblage/decompilation et absence d'index PAK lisible.

Obstacle principal:

- Pour les xrefs code: il faut un desassembleur/decompilateur ARM64 capable de reconstruire les fonctions et cross-references (`Ghidra`, `IDA`, `rizin`, `radare2`).
- Pour les PAK: il faut un outil Unreal PAK-aware et/ou le materiel de cle/index correct.
- Pour les services reseau: il faudrait des logs runtime reussis apres resolution DNS, sans utiliser tokens ni contacter les anciens serveurs pendant cette phase.

## Rapports detailles

- `01_xrefs_serverlist.md`
- `02_loginmgr.md`
- `03_gameserver.md`
- `04_gcloud_rpc.md`
- `05_tdm_route.md`
- `06_launchpak.md`
- `07_pak_crypto.md`

## Conclusion courte

La Phase2 relie les chaines aux tables ELF/UE quand c'est possible, mais les appels runtime critiques restent non resolus. Les preuves actuelles etablissent des metadata server-list/game-server, une route TDataMaster de reporting, et des references PAK non extractibles directement. Elles n'etablissent pas de host GameServer, de structure IP/port, de handler event server-list, ni de cle PAK embarquee.
