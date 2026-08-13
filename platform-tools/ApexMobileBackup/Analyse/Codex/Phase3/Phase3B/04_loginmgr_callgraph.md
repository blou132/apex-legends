# Phase3B - LoginMgr callgraph

## Source

- `Phase3\Phase3B\output\loginmgr_callgraph.json`

## Resultat

UNKNOWN: aucun appel `LoginMgr -> RequestAvatarServerList` n'est confirme.

## Cibles

Toutes les cibles exportees ont 0 xref directe:

- `PureClient/Login/LoginMgr.cpp`: cible `0x21cab3d`, occurrence brute `0x22cab3d`.
- `ULoginMgrWrapper`: cible `0x221434f`, occurrence brute `0x231434f`.
- `LoginMgrWrapper.cpp`: cible `0x22d375d`, occurrence brute `0x23d375d`.
- `OpenServerList`: cible `0x226d0b2`, occurrence brute `0x236d0b2`.
- `ServerListName`: cible `0x2130708`, occurrence brute `0x2230708`.
- `RequestAvatarServerList`: cible `0x21c409f`, occurrence brute `0x22c409f`.

## Graphe

Le graphe exporte retombe sur `FUN_07941d10`, deja identifie comme candidat metadata/reflection, avec 0 caller direct dans l'export.

Aucune fonction exportee ne contient simultanement un indice `LoginMgr`, `OpenServerList`, `ServerListName` et un appel confirme vers `RequestAvatarServerList`.

## Conclusion

Les chaines `LoginMgr`, `OpenServerList`, `ServerListName` et `RequestAvatarServerList` coexistent dans le binaire, mais Phase3B ne prouve pas un chemin d'appel entre elles. Le lien fonctionnel reste UNKNOWN.
