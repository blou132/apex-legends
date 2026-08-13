# Phase2 - 01 - Xrefs server list

## Portee et methode

Fichiers lus en lecture seule:

- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libUE4.so`
- artefacts produits dans `ApexMobileBackup\Analyse\Codex\Phase2\_libUE4_reloc_xrefs.json`
- artefacts produits dans `ApexMobileBackup\Analyse\Codex\Phase2\_libUE4_code_xrefs.json`

Inventaire outils: aucun desassembleur/decompilateur connu n'a ete trouve localement (`ghidra`, `analyzeHeadless`, `jadx`, `apktool`, `radare2`, `r2`, `rizin`, `rz-bin`, `objdump`, `llvm-objdump`, `readelf`, `strings`). L'analyse ci-dessous utilise donc un parseur ELF minimal, les relocations dynamiques, les chaines et une recherche limitee de references AArch64 `ADRP/ADD`.

Sections ELF pertinentes de `libUE4.so`:

| Section | Adresse | Offset fichier | Taille | Notes |
|---|---:|---:|---:|---|
| `.rodata` | `0x20e5780` | `0x20e5780` | `0x74ad14` | chaines cibles |
| `.text` | `0x395a3e0` | `0x39593e0` | `0x6a65394` | code |
| `.data.rel.ro` | `0xa3c3420` | `0xa3c1420` | `0xfb1d28` | tables de metadata/references |
| `.rela.dyn` | `0x72d0` | `0x72d0` | `0x20d5388` | relocations vers `.rodata` |

## Resultats par chaine

| Chaine | Offset / VA | Section | Xrefs confirmees | Conclusion |
|---|---:|---|---|---|
| `RequestAvatarServerList` | `0x21c409f` | `.rodata` | relocations vers `.data.rel.ro` a `0xa8db6c8`, `0xa8e7b70`, `0xa8ecb10`; references code vers metadata a `0x7941d2c` et `0x7941d6c` | Metadata UE/reflection confirmee. Appelant runtime UNKNOWN. |
| `EVENTID_AVATARSERVERLIST_RETURN` | `0x217dbf5` | `.rodata` | aucune relocation directe, aucune xref code detectee | Evenement enum/chaine confirme. Handler UNKNOWN. |
| `OpenServerList` | `0x226d0b2` | `.rodata` | relocation vers `.data.rel.ro` a `0xae66080` | Metadata confirmee. Fonction/runtime UNKNOWN. |
| `ServerListName` | `0x2130708` | `.rodata` | relocation vers `.data.rel.ro` a `0xaa44588` | Propriete/config metadata probable. Aucun endpoint associe. |
| `GameServerBackupIpList` | `0x2180ba5` | `.rodata` | relocations vers `.data.rel.ro` a `0xae65f20`, `0xae65f70` | Metadata de config/propriete confirmee. Site de remplissage UNKNOWN. |
| `SyncPayloadToGameServer` | `0x221f64a` | `.rodata` | relocations vers `.data.rel.ro` a `0xab489d0`, `0xab48ac8`, `0xab48c20`, `0xad87bf8`, `0xad87c18`, `0xad87c68`; refs code metadata a `0x7c1472c`, `0x7c14920`, `0x7c14cec`, `0x7c14e28`, `0x7c14ea8`, `0x7eb815c`, `0x7eb8284`, `0x7eb83a4` | UFUNCTION/RPC-like metadata confirmee. Appel concret UNKNOWN. |

## Contexte proche

`RequestAvatarServerList`:

- Occurrence `.rodata`: `0x21c409f`.
- Relocations:
  - `.rela.dyn` `0xcf7a50` -> `r_offset 0xa8db6c8` (`.data.rel.ro`, fichier `0xa8d96c8`)
  - `.rela.dyn` `0xd05c70` -> `r_offset 0xa8e7b70` (`.data.rel.ro`, fichier `0xa8e5b70`)
  - `.rela.dyn` `0xd0ce28` -> `r_offset 0xa8ecb10` (`.data.rel.ro`, fichier `0xa8eab10`)
- Chaines voisines dans les tables relocalisees: `RandomItemIDGroupByTableNameByStream`, `ReleaseCharacter`, `RequestCharacter`, `WorldContextObject`, `ReturnValue`, `AnimPath`, `CurrentSkinId`.
- Chaines voisines dans `.rodata`: noms globaux melanges (`TeamVoice.cpp`, `CamSocket2`, `ApgameMeleeWeaponComponent.cpp`, `GetNearbyAllyNumber`, etc.). Cette proximite n'est pas une preuve de meme fonction.

`EVENTID_AVATARSERVERLIST_RETURN`:

- Occurrence `.rodata`: `0x217dbf5`.
- Contexte proche: `ELuaLobbyEventType::EVENTID_GAMEUPDATE_CHECK`, `ELuaInGameNetEventType::InGameNetEvent_PingRecovered`, `ELuaCppEventType::EVENTID_ATTACH_SKILL_UI`, `ELuaCppEventType::EVENTID_AVATARSERVERLIST_RETURN`.
- Aucune table ou fonction consommatrice n'a ete identifiee avec les outils disponibles.

`OpenServerList`:

- Occurrence `.rodata`: `0x226d0b2`.
- Relocation: `.rela.dyn` `0x15c1588` -> `r_offset 0xae66080`.
- Chaines voisines relocalisees: `HideLegal`, `OpenPasscode`, `OpenServerList`.
- Le nom peut etre une commande UI/reflection, mais aucun appel runtime n'est prouve.

`ServerListName`:

- Occurrence `.rodata`: `0x2130708`.
- Relocation: `.rela.dyn` `0xf1b298` -> `r_offset 0xaa44588`.
- Chaines voisines relocalisees: `EAutoTraceType`, `AIControlClsKey`, `ServerListName`, `PsoFirstTraceInCurrentGrade`, `TracePointCount`, `PsoSectionIndex`.
- Aucun champ `host`, `ip`, `port`, `region`, `zone` n'est lie a cette entree.

`GameServerBackupIpList`:

- Occurrence `.rodata`: `0x2180ba5`.
- Relocations:
  - `.rela.dyn` `0x15c1498` -> `r_offset 0xae65f20`
  - `.rela.dyn` `0x15c14c8` -> `r_offset 0xae65f70`
- Chaines voisines relocalisees: `AuthFailedLimitMaxCount`, `AuthFailedLimitMinCount`, `AuthFailedTimeInterval`, `GameServerBackupIpList`, `ChannelList`, `ChannelList_Key`.
- `BackupIp` n'apparait comme occurrence exacte que dans cette chaine composee.

`SyncPayloadToGameServer`:

- Occurrence `.rodata`: `0x221f64a`.
- Relocations principales: `0xab489d0`, `0xab48ac8`, `0xab48c20`, `0xad87bf8`, `0xad87c18`, `0xad87c68`.
- Chaines voisines relocalisees: `TeamId`, `ReturnValue`, `TryAddPendingTeamId`, `WhenTeamStateDestroyed`, `LevelCount`, `bSyncEarlyLoadLevel`, `UnitLevelSize`, `LevelOriginLocation`.
- Aucun champ concret `ip`, `host`, `addr`, `address`, `port`, `zone`, `region`, `token`, `ticket`, `session`, `openid`, `uid`, `gameid`, `room`, `match` n'est relie directement a cette entree. Le mot `payload` n'est present ici que dans le nom de fonction.

## Parametres ou champs observables

| Cible | Champs proches confirmes | Champs recherches non confirmes |
|---|---|---|
| `RequestAvatarServerList` | `WorldContextObject`, `ReturnValue`, `AnimPath`, `CurrentSkinId` | `ip`, `host`, `addr`, `port`, `region`, `zone`, `token`, `ticket`, `session`, `openid`, `uid`, `gameid`, `room`, `match` |
| `GameServerBackupIpList` | `AuthFailedLimitMaxCount`, `AuthFailedLimitMinCount`, `AuthFailedTimeInterval`, `ChannelList`, `ChannelList_Key` | valeur d'IP, port, host, site de remplissage |
| `SyncPayloadToGameServer` | `TeamId`, `ReturnValue`, `LevelCount`, `bSyncEarlyLoadLevel`, `UnitLevelSize`, `LevelOriginLocation` | endpoint, socket, IP, port, token, ticket, session |

## Conclusion

Les xrefs exploitables prouvent principalement la presence de metadata Unreal/reflection et de tables relocalisees. Avec les outils actuellement disponibles, aucune fonction nommee appelant `RequestAvatarServerList`, aucun handler de `EVENTID_AVATARSERVERLIST_RETURN`, et aucune structure server-list contenant IP/port n'ont ete identifies.
