# Phase2 - 04 - GCloud RpcAddressSvr

## Portee

Fichiers lus en lecture seule:

- `ApexMobileBackup\Android-data\cache\GCloudSDKLog\GCloud\GCloud_2026081217.log`
- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libgcloud.so`
- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libgcloudcore.so`
- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libUE4.so`
- `ApexMobileBackup\Analyse\APK\resources.arsc`
- assets extraits sous `ApexMobileBackup\Analyse\APK\assets`

Artefact consulte:

- `ApexMobileBackup\Analyse\Codex\Phase2\_gcloud_rpc_context.json`

## Contexte runtime

Log GCloud:

- `GCloud_2026081217.log:2`: version GCloud `2.5.00.200974-Oversea`.
- `GCloud_2026081217.log:3`: gameId `707369824`.
- `GCloud_2026081217.log:4`: `RpcConnectTimeout[15] RpcRetryIncrement[7] RpcAddressSvrPortList[8085|8080] RpcAddressSvrBkIPList[] RpcConnectMode[0] RpcConnectTdrProto[100] RpcParallelChannels[15] RpcParallelCount[1] MapleConnectorConfig[0]`.
- Meme log: `puffer_server` vaut `puffer.4.707369824.dmp.mgapex.com`.
- `address_service.cpp:1004`: parsing DNS de `puffer.4.707369824.dmp.mgapex.com` avec port `0`.
- Lignes suivantes: echec `LocalDNS/getaddrinfo` et echec de construction de la liste d'adresses puffer.

Interpretation:

- Les ports `8085|8080` sont presents dans la config RPC GCloud: CONFIRMED.
- `RpcAddressSvrBkIPList[]` est vide dans ce log: CONFIRMED.
- Le host puffer observe est lie au service puffer/download/config dans le log, avec port parse `0`; il n'est pas prouve comme host de `RpcAddressSvrPortList`.

## Contexte statique libgcloud.so

Occurrences dans `libgcloud.so`:

- `RpcAddressSvrPortList` autour de `0x65736c`.
- `RpcAddressSvrPortList` et config associee autour de `0x691542` / `0x691628`.
- `8085|8080` autour de `0x691558`.

Chaines voisines:

- `RpcTimeout`
- `RpcConnectTimeout`
- `RpcConnectFastTime`
- `RpcConnectMode`
- `RpcConnectTdrProto`
- `RpcRetryIncrement`
- `RpcParallelChannels`
- `RpcParallelCount`
- `RetryTimes`
- `RpcEnableDirectIP`
- `RpcAddressTdrPortList`
- `7618|443`
- `RpcAddressSvrBkIPList`
- `GCloudCommon.cpp`
- `GCloudRemoteConfig.mm`
- `REMOTECONFIG`

Conclusion:

- La source statique des ports par defaut/config GCloud est presente dans `libgcloud.so`: CONFIRMED.
- Le nom de domaine associe a ces ports n'a pas ete trouve en dur dans `libgcloud.so`, `libgcloudcore.so`, `libUE4.so`, `resources.arsc` ou les assets: UNKNOWN.

## Classification du service

Ce qui est confirme:

- `RpcAddressSvrPortList[8085|8080]` appartient au contexte GCloud RPC/address-service.
- `RpcConnectTdrProto[100]` et `RpcParallelChannels[15]` sont dans le meme bloc config.

Ce qui n'est pas confirme:

- Aucun hostname associe a `8085|8080`.
- Aucun lien avec `GameServerBackupIpList`.
- Aucun lien avec `RequestAvatarServerList`.
- Aucun lien avec un serveur de match/gameplay.

## Conclusion

Les ports `8085` et `8080` servent probablement a contacter le service d'adresse RPC GCloud, mais le hostname exact reste UNKNOWN dans les preuves disponibles. Ils ne doivent pas etre classes comme GameServer sans xref ou log supplementaire.
