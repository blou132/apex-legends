# Phase6 - Login storage

## Known metadata

| Property | Offset | Type | Owner | Runtime writer |
| --- | --- | --- | --- | --- |
| `GameServerBackupIpList` | `0x150` CONFIRMED | `TArray<FName>` PROBABLE | `Login` PROBABLE | UNKNOWN |

The property-name metadata is referenced at Ghidra `0xaf65f20`; the probable class metadata `Login` is at `0xaf66180`. Phase6 does not obtain a typed Login instance from the event consumer and does not observe a `Login+0x150` access.

Consequently:

- writing or appending `GameServerBackupIpList`: UNKNOWN;
- clearing/copying the array: UNKNOWN;
- actual meaning of each `FName`: UNKNOWN;
- IP literals as elements: not proven;
- `OpenServerList` trigger after parsing: UNKNOWN.

## Related names

`ChannelList` and `OpenServerList` are present in the same broad Login metadata region from Phase4. `ServerListName` also exists in native metadata. None is accessed by the resolved Phase6 path, so no offset/writer/reader structure can be assigned from the consumer.

The earlier claim that the HTTP callback capture was a Login instance remains **INVALIDATED for that callback**. A type must be proven from the missing consumer before offset `0x150` can be interpreted operationally.

Machine evidence: `output/login_storage.json`.
