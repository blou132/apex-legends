# Phase3B - UEDSToolkit transport

## Source

- `Phase3\Phase3B\output\uedstoolkit_callgraph.json`

## Resultat

UNKNOWN: le transport utilise par `UEDSToolkit` n'est pas confirme.

## Cibles

Toutes les cibles ont 0 xref directe:

- `socket_http.cpp @ 0x2235be7`, occurrence brute `0x2335c36`.
- `DSControllerComponent.cpp @ 0x21c0147`, occurrence brute `0x22c0196`.
- `RegisterDSControllerComponent @ 0x2120940`, occurrence brute `0x2220940`.
- `RegisterDSControllerComponent_metadata @ 0xa98c0b8`.
- `/Script/UEDSToolkit @ 0x226211a`, occurrence brute `0x236211a`.

## Termes transport

Symboles externes presents:

- `<EXTERNAL>::socket`
- `<EXTERNAL>::connect`
- `<EXTERNAL>::send`
- `<EXTERNAL>::recv`
- `<EXTERNAL>::getaddrinfo`

Termes sans hit:

- `curl_easy_`
- `HTTP`

Aucun de ces symboles ou termes n'est relie par xref aux cibles `UEDSToolkit` exportees.

## Fenetre metadata

La fenetre autour de `0xa98c0b8` contient de nombreux pointeurs `.text`, dont:

- `FUN_078a0694`
- `FUN_078a0824`
- `FUN_078a18a4`
- `FUN_078a18fc`
- `FUN_0a000d50`

Cette table ne fournit pas de lien direct vers socket, connect, send, recv, getaddrinfo, curl ou HTTP.

## Conclusion

`socket_http.cpp`, `DSControllerComponent.cpp`, `RegisterDSControllerComponent` et `/Script/UEDSToolkit` sont presents dans le binaire, mais Phase3B ne confirme pas le transport concret. Le transport reste UNKNOWN.
