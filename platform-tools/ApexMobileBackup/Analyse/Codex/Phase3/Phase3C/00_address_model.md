# Phase3C - Modele d'adresses Ghidra

Date locale: 2026-08-13

Mode Ghidra utilise: `-process libUE4.so -noanalysis -readOnly`.

## Conclusion

CONFIRMED: le projet a une image base `0x100000` et les controles `.rodata`, `.text` et `.data.rel.ro` confirment la transformation:

`GHIDRA_ADDRESS = ELF_VIRTUAL_ADDRESS + 0x100000`

## Blocs memoire Ghidra

| Bloc | Debut | Fin | R | W | X |
|---|---:|---:|:---:|:---:|:---:|
| `segment_1.1` | `0x100000` | `0x100317` | YES | NO | NO |
| `.rela.plt` | `0x100318` | `0x10461f` | YES | NO | NO |
| `segment_1.3` | `0x104620` | `0x104fd7` | YES | NO | NO |
| `.gnu.version` | `0x104fd8` | `0x105647` | YES | NO | NO |
| `.gnu.version_d` | `0x105648` | `0x10567f` | YES | NO | NO |
| `.gnu.version_r` | `0x105680` | `0x1056ff` | YES | NO | NO |
| `.gnu.hash` | `0x105700` | `0x105903` | YES | NO | NO |
| `.hash` | `0x105904` | `0x1072cb` | YES | NO | NO |
| `segment_1.9` | `0x1072cc` | `0x1072cf` | YES | NO | NO |
| `.rela.dyn` | `0x1072d0` | `0x21dc657` | YES | NO | NO |
| `.dynstr` | `0x21dc658` | `0x21e1a2a` | YES | NO | NO |
| `segment_1.12` | `0x21e1a2b` | `0x21e577f` | YES | NO | NO |
| `.rodata` | `0x21e5780` | `0x2930493` | YES | NO | NO |
| `.gcc_except_table` | `0x2930494` | `0x293ada3` | YES | NO | NO |
| `.eh_frame_hdr` | `0x293ada4` | `0x2cc7e87` | YES | NO | NO |
| `.eh_frame` | `0x2cc7e88` | `0x3a593db` | YES | NO | NO |
| `.text` | `0x3a5a3e0` | `0xa4bf773` | YES | NO | YES |
| `.plt` | `0xa4bf780` | `0xa4c241f` | YES | NO | YES |
| `.data.rel.ro` | `0xa4c3420` | `0xb475147` | YES | NO | NO |
| `segment_3.2` | `0xb475148` | `0xb47742f` | YES | NO | NO |
| `.note.gnu.build-id` | `0xb477430` | `0xb477453` | YES | NO | NO |
| `segment_3.4` | `0xb477454` | `0xb4776e7` | YES | NO | NO |
| `.got` | `0xb4776e8` | `0xb47d807` | YES | NO | NO |
| `.got.plt` | `0xb47d808` | `0xb47ee5f` | YES | NO | NO |
| `.data` | `0xb47fe60` | `0xb65afc6` | YES | YES | NO |
| `.bss` | `0xb65b000` | `0xb9c10af` | YES | YES | NO |
| `segment_10` | `0xb9c4000` | `0xb9c66ff` | YES | NO | NO |
| `segment_11` | `0xb9c7000` | `0xb9c85b7` | YES | YES | NO |
| `segment_12.1` | `0xb9cc000` | `0xb9cc5b7` | YES | YES | NO |
| `.dynsym` | `0xb9cc5b8` | `0xb9d13ff` | YES | NO | NO |
| `.dynamic` | `0xb9d1400` | `0xb9d16ef` | YES | YES | NO |
| `EXTERNAL` | `0xb9d2000` | `0xb9d377f` | YES | YES | NO |
| `.comment` | `0x0` | `0x3f7` | NO | NO | NO |
| `.shstrtab` | `0x0` | `0xf2` | NO | NO | NO |
| `_elfSectionHeaders` | `0x0` | `0x63f` | NO | NO | NO |

## Verifications d'adresses

| Cible | Type | ELF VA Phase2 | FILE_OFFSET | + imageBase | Bloc direct ancien | Bloc Ghidra | Contenu | Correspondance |
|---|---|---:|---:|---:|---|---|---|:---:|
| RequestAvatarServerList | string | `0x21c409f` | `0x21c409f` | `0x22c409f` | `.rela.dyn` | `.rodata` | ascii:RequestAvatarServerList | YES |
| EVENTID_AVATARSERVERLIST_RETURN | string | `0x217dbf5` | `0x217dbf5` | `0x227dbf5` | `.rela.dyn` | `.rodata` | ascii:EVENTID_AVATARSERVERLIST_RETURN | YES |
| GameServerBackupIpList | string | `0x2180ba5` | `0x2180ba5` | `0x2280ba5` | `.rela.dyn` | `.rodata` | ascii:GameServerBackupIpList | YES |
| SyncPayloadToGameServer | string | `0x221f64a` | `0x221f64a` | `0x231f64a` | `.rodata` | `.rodata` | ascii:SyncPayloadToGameServer | YES |
| /Script/UEDSToolkit | string | `0x226211a` | `0x226211a` | `0x236211a` | `.rodata` | `.rodata` | ascii:/Script/UEDSToolkit | YES |
| OpenServerList | string | `0x226d0b2` | `0x226d0b2` | `0x236d0b2` | `.rodata` | `.rodata` | ascii:OpenServerList | YES |
| ServerListName | string | `0x2130708` | `0x2130708` | `0x2230708` | `.rela.dyn` | `.rodata` | ascii:ServerListName | YES |
| socket_http.cpp | string | `0x2235c36` | `0x2235c36` | `0x2335c36` | `.rodata` | `.rodata` | ascii:socket_http.cpp | YES |
| DSControllerComponent.cpp | string | `0x21c0196` | `0x21c0196` | `0x22c0196` | `.rela.dyn` | `.rodata` | ascii:DSControllerComponent.cpp | YES |
| RegisterDSControllerComponent | string | `0x2120940` | `0x2120940` | `0x2220940` | `.rela.dyn` | `.rodata` | ascii:RegisterDSControllerComponent | YES |
| OnServerAboutToReconnect | string | `0x21f5446` | `0x21f5446` | `0x22f5446` | `.rodata` | `.rodata` | ascii:OnServerAboutToReconnect | YES |
| OnPreReconnectOnServer | string | `0x221d27a` | `0x221d27a` | `0x231d27a` | `.rodata` | `.rodata` | ascii:OnPreReconnectOnServer | YES |
| ClientNotifyReconnectedSuccessfully | string | `0x218a330` | `0x218a330` | `0x228a330` | `.rela.dyn` | `.rodata` | ascii:ClientNotifyReconnectedSuccessfully | YES |
| RequestAvatarServerList code ref 1 | code | `0x7941d2c` | `0x7940d2c` | `0x7a41d2c` | `.text` | `.text` | instruction:adrp x1,0xa9e7000 | YES |
| RequestAvatarServerList code ref 2 | code | `0x7941d6c` | `0x7940d6c` | `0x7a41d6c` | `.text` | `.text` | instruction:adrp x1,0xa9e7000 | YES |
| SyncPayloadToGameServer code ref 1 | code | `0x7c1472c` | `0x7c1372c` | `0x7d1472c` | `.text` | `.text` | instruction:adrp x1,0xac48000 | YES |
| SyncPayloadToGameServer code ref 6 | code | `0x7eb815c` | `0x7eb715c` | `0x7fb815c` | `.text` | `.text` | instruction:adrp x1,0xae87000 | YES |
| RequestAvatar metadata 1 | metadata | `0xa8db6c8` | `0xa8d96c8` | `0xa9db6c8` | `.data.rel.ro` | `.data.rel.ro` | u64:0x22c409f -> .rodata ascii:RequestAvatarServerList | YES |
| RequestAvatar metadata 2 | metadata | `0xa8e7b70` | `0xa8e5b70` | `0xa9e7b70` | `.data.rel.ro` | `.data.rel.ro` | u64:0x22c409f -> .rodata ascii:RequestAvatarServerList | YES |
| GameServerBackup metadata 1 | metadata | `0xae65f20` | `0xae63f20` | `0xaf65f20` | `.data.rel.ro` | `.data.rel.ro` | u64:0x2280ba5 -> .rodata ascii:GameServerBackupIpList | YES |
| SyncPayload metadata 1 | metadata | `0xab489d0` | `0xab469d0` | `0xac489d0` | `.data.rel.ro` | `.data.rel.ro` | u64:0x231f64a -> .rodata ascii:SyncPayloadToGameServer | YES |
| UEDSToolkit metadata | metadata | `0xa98c0b8` | `0xa98a0b8` | `0xaa8c0b8` | `.data.rel.ro` | `.data.rel.ro` | u64:0x2220940 -> .rodata ascii:RegisterDSControllerComponent | YES |

Le JSON complet est dans `output/address_mapping.json`.
