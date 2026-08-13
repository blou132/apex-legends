# Phase3 - 07 - UEDSToolkit

## Cibles

Phase2:

- `/Script/UEDSToolkit`: `0x226211a`
- `DSControllerComponent.cpp`: autour de `0x21c0147`
- `socket_http.cpp`: autour de `0x2235be7`
- `RegisterDSControllerComponent`: `0x2120940`
- Metadata `RegisterDSControllerComponent`: `0xa98c0b8`

## Resultat Ghidra

Ghidra a importe `libUE4.so`, mais aucun export de xrefs/callgraph autour de `socket_http.cpp` ou `RegisterDSControllerComponent` n'a ete obtenu.

Ce qui est confirme:

- UEDSToolkit est dans le binaire client Android.
- `socket_http.cpp` est un chemin source compile dans le client.
- `RegisterDSControllerComponent` est une entree metadata/reflection.

Ce qui reste inconnu:

- fonction C++ concrete associee a `socket_http.cpp`;
- callers/callees;
- appels vers `socket`, `connect`, `getaddrinfo`, `send`, `recv`, `sendto`, `recvfrom`;
- appels vers `curl_easy_*`;
- protocole HTTP/HTTPS concret.

## Client ou Dedicated Server

| Question | Statut | Preuve |
|---|---|---|
| UEDSToolkit present cote client | CONFIRMED | chaines et `/Script/UEDSToolkit` dans `libUE4.so` importe depuis l'APK client |
| UEDSToolkit utilise seulement Dedicated Server | UNKNOWN | nom `DS` non suffisant |
| UEDSToolkit utilise cote client runtime | UNKNOWN | presence dans le binaire ne prouve pas execution runtime |
| UEDSToolkit utilise dans les deux | UNKNOWN | pas de callgraph |

## Transport

Statut: UNKNOWN.

Les imports reseau generiques existent dans `libUE4.so` selon Phase2 (`socket`, `connect`, `getaddrinfo`, `send`, `recv`, etc.), mais Phase3 n'a pas pu relier ces imports a UEDSToolkit par xref ou pseudo-code.

## Conclusion

Le transport reel UEDSToolkit n'est pas identifie. La Phase3 confirme l'import Ghidra et la presence des symboles/source paths, mais ne relie pas encore `UEDSToolkit -> fonction C++ -> transport reseau concret`.
