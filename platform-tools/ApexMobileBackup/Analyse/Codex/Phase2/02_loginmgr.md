# Phase2 - 02 - LoginMgr

## Portee

Fichier principal:

- `ApexMobileBackup\Analyse\APK\lib\arm64-v8a\libUE4.so`

Artefacts consultes:

- `ApexMobileBackup\Analyse\Codex\Phase2\_libUE4_reloc_xrefs.json`
- `ApexMobileBackup\Analyse\Codex\Phase2\_libUE4_code_xrefs.json`

## Chaines LoginMgr identifiees

| Chaine | Offset / VA | Source de preuve | Notes |
|---|---:|---|---|
| `PureClient/Login/LoginMgr.cpp` | `0x21cab3d` | `.rodata` / `libUE4-strings.txt` ligne 30196 | chemin source compile dans le client |
| `ULoginMgrWrapper` | `0x221434f` | `.rodata` / `libUE4-strings.txt` ligne 40303 | classe/wrapper UE visible |
| `LoginMgr` | `0x2214350` | sous-chaine de `ULoginMgrWrapper` | pas une occurrence autonome prouvee ici |
| `LoginMgrWrapper.cpp` | `0x22d375d` | `.rodata` / `libUE4-strings.txt` ligne 65946 | chemin source compile dans le client |

Contexte proche de `PureClient/Login/LoginMgr.cpp`:

- `GetFirstClientFrontendHUD`
- `EFrontendMapStatus::InitMap`
- `GetMapPath`
- `StateURL`

Contexte proche de `ULoginMgrWrapper`:

- `LogReport.cpp`
- noms UI/audio melanges dans `.rodata`

## Graphe demande

| Transition | Statut | Preuve |
|---|---|---|
| `LoginMgr` present dans le binaire client | CONFIRMED | `PureClient/Login/LoginMgr.cpp` a `0x21cab3d`, `ULoginMgrWrapper` a `0x221434f`, `LoginMgrWrapper.cpp` a `0x22d375d` |
| `LoginMgr` -> `RequestAvatarServerList` | UNKNOWN | aucune xref directe detectee entre les chaines LoginMgr et `RequestAvatarServerList` |
| `RequestAvatarServerList` -> evenement retour | UNKNOWN | `RequestAvatarServerList` a des xrefs metadata; pas de liaison vers `EVENTID_AVATARSERVERLIST_RETURN` |
| `EVENTID_AVATARSERVERLIST_RETURN` -> handler | UNKNOWN | occurrence `.rodata` a `0x217dbf5`, sans relocation/xref code detectee |
| handler -> `OpenServerList` | UNKNOWN | `OpenServerList` a une relocation metadata a `0xae66080`, sans appel prouve |
| `OpenServerList` ouvre la liste de serveurs | UNKNOWN | nom compatible avec UI, mais aucun corps de fonction ni caller identifie |

Representation:

```text
LoginMgr
  -> UNKNOWN
  -> RequestAvatarServerList
  -> UNKNOWN
  -> EVENTID_AVATARSERVERLIST_RETURN
  -> UNKNOWN
  -> OpenServerList
```

## Structure de reponse et region

Aucun element suffisant n'a ete trouve pour reconstruire une structure de reponse server-list. Les champs recherches (`ip`, `host`, `addr`, `address`, `server`, `port`, `zone`, `region`, `token`, `ticket`, `session`, `openid`, `uid`, `gameid`, `room`, `match`, `payload`) ne sont pas relies par xref directe aux chaines LoginMgr ou server-list.

Elements observables:

- `ServerListName` existe a `0x2130708`, avec relocation vers `0xaa44588`.
- `OpenServerList` existe a `0x226d0b2`, avec relocation vers `0xae66080`.
- `GameServerBackupIpList` existe a `0x2180ba5`, avec relocations vers `0xae65f20` et `0xae65f70`.

Ces elements confirment des noms de proprietes/fonctions exposes dans les metadata, mais pas le flux runtime complet login -> server list.

## Conclusion

`LoginMgr` et `ULoginMgrWrapper` sont bien presents dans `libUE4.so`, mais le lien fonctionnel vers `RequestAvatarServerList`, l'evenement `EVENTID_AVATARSERVERLIST_RETURN`, et `OpenServerList` reste UNKNOWN sans desassembleur/decompilateur capable de reconstruire les fonctions et les xrefs code completes.
