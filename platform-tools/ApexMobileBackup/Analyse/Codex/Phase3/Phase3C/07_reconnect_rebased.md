# Phase3C - Reconnect rebased

Statut modele d'adresses: CONFIRMED.

## Cibles rebased

| Cible | Type | ELF VA | Ghidra VA | Bloc | Contenu | Xrefs | Relocs | Raw pointers |
|---|---|---:|---:|---|---|---:|---:|---:|
| OnServerAboutToReconnect string | string | `0x21f5446` | `0x22f5446` | `.rodata` | ascii:OnServerAboutToReconnect | 0 | 3 | 3 |
| OnPreReconnectOnServer string | string | `0x221d27a` | `0x231d27a` | `.rodata` | ascii:OnPreReconnectOnServer | 0 | 4 | 4 |
| ClientNotifyReconnectedSuccessfully string | string | `0x218a330` | `0x228a330` | `.rodata` | ascii:ClientNotifyReconnectedSuccessfully | 0 | 3 | 3 |
| OnServerAboutToReconnect metadata 1 | metadata | `0xad72b80` | `0xae72b80` | `.data.rel.ro` | ascii:FT/ | 0 | 79 | 0 |
| OnServerAboutToReconnect metadata 2 | metadata | `0xad72c18` | `0xae72c18` | `.data.rel.ro` | ascii:FT/ | 0 | 69 | 1 |
| OnServerAboutToReconnect metadata 3 | metadata | `0xad72e40` | `0xae72e40` | `.data.rel.ro` | ascii:FT/ | 0 | 56 | 0 |
| OnPreReconnectOnServer metadata 1 | metadata | `0xa966210` | `0xaa66210` | `.data.rel.ro` | u64:0x231d27a -> .rodata ascii:OnPreReconnectOnServer | 0 | 80 | 0 |
| OnPreReconnectOnServer metadata 2 | metadata | `0xa967210` | `0xaa67210` | `.data.rel.ro` | u64:0x231d27a -> .rodata ascii:OnPreReconnectOnServer | 0 | 37 | 0 |
| OnPreReconnectOnServer metadata 3 | metadata | `0xa969838` | `0xaa69838` | `.data.rel.ro` | u64:0x231d27a -> .rodata ascii:OnPreReconnectOnServer | 0 | 80 | 0 |
| ClientNotifyReconnectedSuccessfully metadata 1 | metadata | `0xa9ae008` | `0xaaae008` | `.data.rel.ro` | u64:0x228a330 -> .rodata ascii:ClientNotifyReconnectedSuccessfully | 0 | 80 | 0 |
| ClientNotifyReconnectedSuccessfully metadata 2 | metadata | `0xa9b0698` | `0xaab0698` | `.data.rel.ro` | u64:0x228a330 -> .rodata ascii:ClientNotifyReconnectedSuccessfully | 0 | 49 | 0 |
| ClientNotifyReconnectedSuccessfully metadata 3 | metadata | `0xa9ba988` | `0xaaba988` | `.data.rel.ro` | u64:0x228a330 -> .rodata ascii:ClientNotifyReconnectedSuccessfully | 0 | 80 | 0 |

## Fonctions corrigees

UNKNOWN: aucune fonction contenant les adresses corrigees n'a ete trouvee.

## Recherches texte

| Terme | Occurrences | Premieres adresses |
|---|---:|---|
| `OnServerAboutToReconnect` | 1 | 0x22f5446 |
| `OnPreReconnectOnServer` | 1 | 0x231d27a |
| `ClientNotifyReconnectedSuccessfully` | 1 | 0x228a330 |
| `Host` | 6 | 0x226ba36, 0x226ec2b, 0x2292074, 0x22d2ed8, 0x230d623, 0x230eb64 |
| `Port` | 10 | 0x222082c, 0x225a7bb, 0x226ba2e, 0x228b9b7, 0x22d1a98, 0x22e4e27, 0x231ee9d, 0x235e1ab, ... |
| `RemoteAddr` | 0 | - |
| `IpAddr` | 0 | - |
| `InternetAddr` | 0 | - |
| `ServerConnection` | 1 | 0x22becf9 |
| `PendingNetGame` | 2 | 0x2291707, 0x22ccf7f |
| `FURL` | 0 | - |
| `URL` | 0 | - |
| `getaddrinfo` | 2 | 0x21ddfee, 0x22a5096 |
| `socket` | 4 | 0x21ddd1c, 0x2275f04, 0x22b176f, 0x22c05c9 |

JSON detaille: `output/reconnect_rebased.json`.
