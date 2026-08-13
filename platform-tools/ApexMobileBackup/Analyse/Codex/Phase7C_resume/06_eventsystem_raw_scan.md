# Phase7C Resume - EventSystem raw scan

The four local PAK copies were scanned read-only for all requested terms in ASCII/UTF-8 and UTF-16LE. No decryption, decompression, brute force, or carving was attempted.

## Target result

None of the following produced a raw hit:

- `Script/Tools/EventSystem/EventSystem.lua`
- `Tools/EventSystem/EventSystem.lua`
- `EventSystem.lua`
- `EventSystem.PostCppEvent`
- `PostCppEvent`
- `EVENTID_AVATARSERVERLIST_RETURN`
- `ELuaCppEventType::EVENTID_AVATARSERVERLIST_RETURN`
- `AvatarServerList`
- `RequestAvatarServerList`
- `OpenServerList`
- `ServerListName`

```text
EVENTSYSTEM_NOT_FOUND_IN_RAW = YES
POSTCPPEVENT_NOT_FOUND_IN_RAW = YES
EVENTID_NOT_FOUND_IN_RAW = YES
ENTRY_BOUNDARY = UNKNOWN
```

This proves absence of those exact byte sequences in the four recovered PAK bodies. It does not prove that the logical module is absent: its name or content may be indexed, encoded, compressed, encrypted, or supplied by another virtual-file backend.

## Historical witnesses

| PAK | Witness | Offset | Encoding | Result |
| --- | --- | ---: | --- | --- |
| `launch.pak` | `Client/Launch/ClientLaunch.lua` | `0x250517` | ASCII/UTF-8 | confirmed, exact historical offset |
| `pakchunk3-Android_ASTC.pak` | `Client/UMG/SelectLegend/BP_Screen.lua` | `0x68080b2b` | ASCII/UTF-8 | confirmed |
| `pakchunk3-Android_ASTC.pak` | `RatingSeasonArchivesLogic.lua` | `0x6b33ffff` | ASCII/UTF-8 | confirmed within full historical path |

```text
BUILD_WITNESS_MATCH = YES
```

Short 64-byte contexts, local entropy, and nearby readable strings are retained in `output/eventsystem_raw_scan.json`.
