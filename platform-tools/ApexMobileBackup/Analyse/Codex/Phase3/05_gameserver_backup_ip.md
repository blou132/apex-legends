# Phase3 - 05 - GameServerBackupIpList

## Cible

Phase2:

- `GameServerBackupIpList`: `0x2180ba5`
- Metadata:
  - `0xae65f20`
  - `0xae65f70`
- Contexte relocalise:
  - `AuthFailedLimitMaxCount`
  - `AuthFailedLimitMinCount`
  - `AuthFailedTimeInterval`
  - `ChannelList`
  - `ChannelList_Key`

## Resultat Ghidra

Ghidra a importe le programme et exporte les entrees de fonctions, mais aucun export Ghidra de references vers `0x2180ba5`, `0xae65f20` ou `0xae65f70` n'a ete obtenu.

Les adresses `0xae65f20` et `0xae65f70` sont dans une zone metadata/data, pas dans une fonction executee candidate issue de `ghidra_nearest_functions.json`.

## Classe/structure proprietaire

Statut: UNKNOWN.

Preuves:

- Metadata de propriete/config: CONFIRMED par Phase2.
- Classe proprietaire: non exportee.
- Type exact (`FString`, `TArray<FString>`, `TArray<...>`, map, autre): UNKNOWN.

## Site de remplissage

Statut: UNKNOWN.

Recherche demandee:

- fonctions qui lisent la propriete;
- fonctions qui ecrivent la propriete;
- usages `IP`, `Port`, `Address`, `Host`, `Server`, `Backup`, `ChannelList`, `AuthFailed`;
- conversions `inet_addr`, `inet_pton`, `getaddrinfo`, `sockaddr`, `sockaddr_in`.

Resultat:

- Aucun site d'ecriture n'a ete confirme.
- Aucun appel `inet_*`/`getaddrinfo`/`sockaddr` n'a ete rattache a `GameServerBackupIpList`.
- Aucun lien `GameServerBackupIpList -> connexion socket` n'a ete confirme.

## Conclusion

`GameServerBackupIpList` reste une propriete/metadata confirmee, mais sa classe proprietaire, son type exact et son code de remplissage restent UNKNOWN.
