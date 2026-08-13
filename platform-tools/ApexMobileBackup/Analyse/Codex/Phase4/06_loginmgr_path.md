# Phase4 - Chemin LoginMgr

Les occurrences exactes corrigees sont toutes validees:

| Chaine | Adresse Ghidra | Pointeurs/relocations |
|---|---:|---:|
| PureClient/Login/LoginMgr.cpp | `0x22cab3d` | 0 |
| ULoginMgrWrapper | `0x231434f` | 0 |
| LoginMgrWrapper.cpp | `0x23d375d` | 0 |
| RequestAvatarServerList | `0x22c409f` | 3 |
| OpenServerList | `0x236d0b2` | 1 |
| ServerListName | `0x2230708` | 1 |

Les trois anciennes valeurs `0x100000` sont **INVALIDATED**.

## Conclusion

`LoginMgr -> RequestAvatarServerList` reste **UNKNOWN**:

- les chaines source `LoginMgr` n'ont ni pointeur brut ni relocation;
- `RequestAvatarServerList` possede une registration native confirmee;
- `OpenServerList` et `GameServerBackupIpList` se trouvent dans la metadata de la classe `Login`;
- aucune fonction commune ou arrete de callgraph ne relie encore les deux ensembles.

La proximite semantique et la classe `Login` rendent le lien plausible, mais pas prouve.

Export: `output/loginmgr_path.json`.
