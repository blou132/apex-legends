# Phase3 - 04 - LoginMgr

## Cibles

Chaines connues:

| Chaine | Offset Phase2 |
|---|---:|
| `PureClient/Login/LoginMgr.cpp` | `0x21cab3d` |
| `ULoginMgrWrapper` | `0x221434f` |
| `LoginMgrWrapper.cpp` | `0x22d375d` |
| `RequestAvatarServerList` | `0x21c409f` |
| `OpenServerList` | `0x226d0b2` |
| `ServerListName` | `0x2130708` |
| `EVENTID_AVATARSERVERLIST_RETURN` | `0x217dbf5` |

## Resultat Ghidra

Ghidra a importe `libUE4.so` et exporte `467079` fonctions, mais l'export disponible ne contient pas les xrefs data/code autour des chaines LoginMgr.

Les scripts locaux destines a:

- localiser les references des chaines;
- obtenir la fonction englobante;
- lister callers/callees;
- exporter le pseudo-code;

n'ont pas pu etre executes avec `analyzeHeadless.bat` pour les raisons documentees dans `00_installation.md`.

## Graphe demande

| Transition | Statut Phase3 | Preuve |
|---|---|---|
| `LoginMgr` present dans le client | CONFIRMED | chaines `PureClient/Login/LoginMgr.cpp`, `ULoginMgrWrapper`, `LoginMgrWrapper.cpp` dans `libUE4.so` |
| `LoginMgr` -> `RequestAvatarServerList` | UNKNOWN | aucune xref Ghidra exportee |
| `RequestAvatarServerList` -> backend/SDK | UNKNOWN | metadata/reflection seulement |
| callback -> `EVENTID_AVATARSERVERLIST_RETURN` | UNKNOWN | handler non identifie |
| `EVENTID_AVATARSERVERLIST_RETURN` -> `OpenServerList` | UNKNOWN | pas de xref handler |
| `OpenServerList` ouvre l'UI server list | UNKNOWN | nom compatible, preuve runtime absente |

Representation:

```text
LoginMgr
  -> UNKNOWN
RequestAvatarServerList
  -> UNKNOWN
backend/SDK
  -> UNKNOWN
callback
  -> UNKNOWN
EVENTID_AVATARSERVERLIST_RETURN
  -> UNKNOWN
OpenServerList
```

## Conclusion

La Phase3 ne confirme pas que `LoginMgr` appelle reellement `RequestAvatarServerList`. Les chaines LoginMgr et server-list restent presentes dans le client, mais le flux login -> server list reste UNKNOWN.
