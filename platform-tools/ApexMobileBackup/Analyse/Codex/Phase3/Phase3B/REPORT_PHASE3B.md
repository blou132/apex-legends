# Phase3B - Rapport final

Date locale: 2026-08-13

## Portee

Phase3B a corrige l'execution Java headless Ghidra puis a lance un export cible sur le projet existant:

- projet: `Phase3\ghidra_project\ApexMobileUE4`;
- programme: `libUE4.so`;
- mode: `-process libUE4.so -noanalysis -readOnly`;
- Ghidra: `C:\Tools\ghidra_12.1.2_PUBLIC`;
- script cible: `Phase3\ghidra_scripts\ApexPhase3TargetExport.java`.

Aucun reimport et aucune analyse automatique longue n'ont ete relances.

## Exports JSON valides

- `Phase3\Phase3B\output\request_avatar_xrefs.json`
- `Phase3\Phase3B\output\request_avatar_metadata.json`
- `Phase3\Phase3B\output\avatar_event_xrefs.json`
- `Phase3\Phase3B\output\loginmgr_callgraph.json`
- `Phase3\Phase3B\output\gameserver_backup_xrefs.json`
- `Phase3\Phase3B\output\syncpayload_callgraph.json`
- `Phase3\Phase3B\output\reconnect_callgraph.json`
- `Phase3\Phase3B\output\uedstoolkit_callgraph.json`

Validation finale:

```text
OK avatar_event_xrefs.json targets=1 functions=0
OK gameserver_backup_xrefs.json targets=3 functions=0
OK loginmgr_callgraph.json targets=8 functions=90
OK reconnect_callgraph.json targets=13 functions=90
OK request_avatar_metadata.json targets=3 functions=90
OK request_avatar_xrefs.json targets=6 functions=90
OK syncpayload_callgraph.json targets=7 functions=90
OK uedstoolkit_callgraph.json targets=5 functions=0
```

## Observation importante

Plusieurs adresses de chaines issues des phases precedentes pointent vers `.rela.dyn`, tandis que l'occurrence brute exploitable apparait souvent a `+0x100000`, ce qui correspond a l'image base Ghidra.

Exemples:

- `RequestAvatarServerList`: cible `0x21c409f`, occurrence `0x22c409f`.
- `EVENTID_AVATARSERVERLIST_RETURN`: cible `0x217dbf5`, occurrence `0x227dbf5`.
- `GameServerBackupIpList`: cible `0x2180ba5`, occurrence `0x2280ba5`.
- `SyncPayloadToGameServer`: cible `0x221f64a`, occurrence `0x231f64a`.
- `/Script/UEDSToolkit`: cible `0x226211a`, occurrence `0x236211a`.

Ces occurrences ont 0 xref dans les exports finaux.

## Reponses aux 15 questions

1. Execution Java headless Ghidra: CONFIRMED. `TestPhase3Script.java` affiche `PHASE3_SCRIPT_OK`, `libUE4.so`, `AARCH64:LE:64:v8A`.

2. Fonction native de `RequestAvatarServerList`: UNKNOWN. La chaine et les metadata n'ont pas de xref exploitable. `FUN_07941d10` reste candidat structurel seulement.

3. Role de `FUN_07941d10`: PROBABLE metadata/reflection helper. La decompilation montre parcours et initialisation d'objet; l'export ne confirme pas une implementation runtime de `RequestAvatarServerList`.

4. Pointeur natif associe a `RequestAvatarServerList`: UNKNOWN. Aucun slot metadata n'est confirme comme `NativeFunc`.

5. Valeur numerique de `EVENTID_AVATARSERVERLIST_RETURN`: UNKNOWN. Aucune table enum, comparaison ou switch n'est relie a la chaine.

6. Handler de `EVENTID_AVATARSERVERLIST_RETURN`: UNKNOWN. Aucun dispatcher ou handler n'est relie par xref.

7. `LoginMgr` appelle-t-il `RequestAvatarServerList`: UNKNOWN. Les chaines `LoginMgr`, `OpenServerList`, `ServerListName` et `RequestAvatarServerList` ont 0 xref.

8. Classe proprietaire de `GameServerBackupIpList`: UNKNOWN. Les metadata candidates ne sont pas probantes et pointent vers une zone UI/ping.

9. Type exact de `GameServerBackupIpList`: UNKNOWN. Aucun hit `ArrayProperty`, `StrProperty`, `NameProperty`, `StructProperty` ou `MapProperty`.

10. Code writer de `GameServerBackupIpList`: UNKNOWN. Les symboles `inet_addr`, `inet_pton`, `getaddrinfo` existent, mais ne sont pas relies a cette propriete.

11. `SyncPayloadToGameServer` est-il un RPC Unreal standard: UNKNOWN. Le nom suggere un flux reseau, mais aucun hit `ProcessEvent`, `ProcessRemoteFunction`, `UNetDriver`, `UNetConnection`, `UChannel`, `SendBunch`.

12. Fonction native de `SyncPayloadToGameServer`: UNKNOWN. Les fonctions `FUN_07c14710`, `FUN_07c148b0`, `FUN_07c14cac`, `FUN_07c14e44`, `FUN_07eb80dc`, `FUN_07eb8224`, `FUN_07eb8364` sont candidates metadata/serialization, pas des natives confirmees.

13. La reconnexion revele-t-elle l'adresse game server: UNKNOWN. Aucun hit direct `Host`, `Port`, `RemoteAddr`, `IpAddr`, `InternetAddr`, `GetRemoteAddr`.

14. Transport `UEDSToolkit`: UNKNOWN. `socket_http.cpp`, `DSControllerComponent.cpp`, `RegisterDSControllerComponent` et `/Script/UEDSToolkit` sont presents, mais aucun lien vers socket/connect/send/recv/getaddrinfo/curl/HTTP n'est confirme.

15. Trace `server list -> addr/port -> connection`: UNKNOWN. Phase3B ne relie pas `RequestAvatarServerList`, `GameServerBackupIpList`, reconnect, ni UEDSToolkit a une structure adresse/port ou a un appel de connexion.

## Conclusion globale

Phase3B confirme que la voie Java Ghidra est reparee et que l'export cible fonctionne. Les resultats techniques sont majoritairement negatifs: les chaines et metadata recherchees existent, mais les xrefs attendues ne sont pas presentes dans l'etat actuel du projet Ghidra. Les hypotheses reseau ou RPC doivent donc rester UNKNOWN sauf mention explicite PROBABLE pour le role metadata/reflection de `FUN_07941d10`.
