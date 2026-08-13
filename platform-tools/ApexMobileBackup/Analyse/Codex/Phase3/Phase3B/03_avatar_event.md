# Phase3B - EVENTID_AVATARSERVERLIST_RETURN

## Source

- `Phase3\Phase3B\output\avatar_event_xrefs.json`

## Resultat

UNKNOWN: la valeur numerique de `EVENTID_AVATARSERVERLIST_RETURN` n'est pas confirmee.

UNKNOWN: le handler qui traite cet evenement n'est pas confirme.

## Elements confirmes

- Cible Phase2: `EVENTID_AVATARSERVERLIST_RETURN_string @ 0x217dbf5`, bloc `.rela.dyn`, aucune xref.
- Occurrence brute: `0x227dbf5`, preview `EVENTID_AVATARSERVERLIST_RETURN`, aucune xref.
- Aucune fonction n'a ete exportee pour ce groupe.

## Recherche ELua

La recherche `ELuaCppEventType::` retourne 80 occurrences limitees, toutes sans xref. Exemples:

- `0x22052e4`: `ELuaCppEventType::EVENTID_HEALTH_VALUE`
- `0x220530b`: `ELuaCppEventType::EVENTID_AIM_CHANGE`
- `0x220549d`: `ELuaCppEventType::EVENTID_UDP_PING_ONE_FINISHED`
- `0x2250f02`: `ELuaCppEventType::EVENTID_NETWORK_STATE_CHANGE_NOTIFY`

L'export ne confirme pas de forme `ELuaCppEventType::EVENTID_AVATARSERVERLIST_RETURN`.

## Recherche handler

Les termes suivants ne produisent pas d'occurrence:

- `LuaCppEvent`
- `DispatchEvent`
- `SendEvent`
- `OnEvent`
- `TriggerEvent`

## Conclusion

La chaine evenement existe, mais aucune table enum, aucune comparaison numerique, aucun switch et aucun dispatcher lie par xref n'a ete confirme dans cet export. Valeur numerique et handler restent UNKNOWN.
