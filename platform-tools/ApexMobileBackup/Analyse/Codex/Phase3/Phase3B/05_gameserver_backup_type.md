# Phase3B - GameServerBackupIpList type and writer

## Source

- `Phase3\Phase3B\output\gameserver_backup_xrefs.json`

## Resultat

UNKNOWN: la classe proprietaire de `GameServerBackupIpList` n'est pas confirmee.

UNKNOWN: le type exact de la propriete n'est pas confirme.

UNKNOWN: le code qui remplit ou ecrit cette liste n'est pas confirme.

## Elements confirmes

- Cible Phase2: `GameServerBackupIpList_string @ 0x2180ba5`, bloc `.rela.dyn`, aucune xref.
- Occurrence brute: `0x2280ba5`, preview `GameServerBackupIpList`, aucune xref.
- Metadata candidates:
  - `0xae65f20`, bloc `.data.rel.ro`, aucune xref;
  - `0xae65f70`, bloc `.data.rel.ro`, aucune xref.

## Recherche type

Les termes de type Unreal recherches ne donnent aucun hit:

- `ArrayProperty`
- `StrProperty`
- `NameProperty`
- `StructProperty`
- `MapProperty`

Donc Phase3B ne permet pas de choisir entre `FString`, `TArray<FString>`, map, struct, ou autre conteneur.

## Recherche reseau

Les symboles externes existent:

- `<EXTERNAL>::inet_addr @ 0x2b4`
- `<EXTERNAL>::inet_pton @ 0x96`
- `<EXTERNAL>::getaddrinfo @ 0xc2`

Mais aucune fonction du groupe n'est liee a ces symboles par xref ou graphe. `sockaddr` n'a pas de hit.

## Fenetre metadata

La fenetre autour de `0xae65f20` / `0xae65f70` ne confirme pas `GameServerBackupIpList`. Elle contient plutot des noms UI / ping, par exemple:

- `IsFindPingIndex`
- `IsFindPingIndexForPingWidget`
- `OnClickHeadIcon`
- `OnPlayerUseItem`
- `OnTeamInfoSync`

Cette zone ne supporte pas une attribution forte a `GameServerBackupIpList`.

## Conclusion

La chaine est presente, mais les metadata candidates ne sont pas probantes. Classe, type exact et writer restent UNKNOWN.
