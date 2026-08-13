# Phase3B - Reconnect deep dive

## Source

- `Phase3\Phase3B\output\reconnect_callgraph.json`

## Resultat

UNKNOWN: le code de reconnexion ne revele pas d'adresse serveur, d'hote, de port ou de structure serveur confirmee dans cet export.

## Cibles

Toutes les cibles ont 0 xref directe:

- `ReconnectSyncData_string @ 0x2141b07`, occurrence brute `0x224dab0`.
- `OnServerAboutToReconnect_string @ 0x21f5446`, occurrence brute `0x22f5446`.
- `OnPreReconnectOnServer_string @ 0x221d27a`, occurrence brute `0x231d27a`.
- `ClientNotifyReconnectedSuccessfully_string @ 0x218a330`, occurrence brute `0x228a330`.
- Metadata `OnServerAboutToReconnect`: `0xad72b80`, `0xad72c18`, `0xad72e40`.
- Metadata `OnPreReconnectOnServer`: `0xa966210`, `0xa967210`, `0xa969838`.
- Metadata `ClientNotifyReconnectedSuccessfully`: `0xa9ae008`, `0xa9b0698`, `0xa9ba988`.

## Termes reseau

Termes sans occurrence brute:

- `UNetConnection`
- `Host`
- `Port`
- `RemoteAddr`
- `IpAddr`
- `InternetAddr`
- `GetRemoteAddr`
- `ServerConnection`

Termes avec occurrences non liees:

- `WorldContext @ 0x220179e`, 0 xref.
- `PendingNetGame.cpp @ 0x2291707`, 0 xref.
- `PendingNetGameCreateFailure @ 0x22ccf7f`, 0 xref.

Symboles externes presents mais non relies au graphe:

- `<EXTERNAL>::getaddrinfo`
- `<EXTERNAL>::socket`

## Fonctions candidates

Fonctions racines principales exportees:

- `FUN_079c3420 @ 0x79c3420`
- `FUN_079c90bc @ 0x79c90bc`
- `FUN_07e9ffe4 @ 0x7e9ffe4`
- `FUN_07ea03fc @ 0x7ea03fc`
- `FUN_07ea059c @ 0x7ea059c`
- `FUN_07ea05dc @ 0x7ea05dc`
- `FUN_07ea0694 @ 0x7ea0694`
- `FUN_07a1a7c8 @ 0x7a1a7c8`

Exemple de wrapper lazy-init:

```c
long FUN_07e9ffe4(void)
{
  if (lRam000000000b783e28 == 0) {
    FUN_04763790(&UNK_027b04b8,&UNK_024f3946,0xb783e28,FUN_0a25c24c,0x248,8,0x10000000,0,
                 &UNK_025c681e,FUN_07ea00cc,FUN_0a25c250,FUN_083e660c,FUN_065d335c,FUN_06303f78,0);
  }
  return lRam000000000b783e28;
}
```

Ces fonctions ne montrent pas de hit direct vers `RemoteAddr`, `Host`, `Port`, `getaddrinfo`, `socket` ou une structure d'adresse serveur.

## Conclusion

Les noms de reconnexion sont presents, mais les xrefs et la decompilation ciblee ne confirment pas la conservation ou l'utilisation d'une adresse/port de game server. L'existence d'une trace `server list -> addr/port -> connection` reste UNKNOWN.
