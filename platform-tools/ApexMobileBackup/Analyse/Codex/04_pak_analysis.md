# PAK analysis

No PAK/OBB/APK file was modified. The PAK inspection used footer parsing and raw string searches only. No Unreal-specific tool was installed.

## PAK footer summary

| PAK | Size | Footer offset | Magic | Version | Index offset | Index size | Compression strings | Index status |
| --- | ---: | ---: | --- | ---: | ---: | ---: | --- | --- |
| `Analyse/MAIN/AClient/Content/Paks/launch.pak` | 140867491 | `0x86576d7` | `0x5A6F12E1` | 8 | `0x851a746` | 1298304 | `Oodle`, `Zlib` | appears encrypted/unreadable |
| `Analyse/MAIN/AClient/Content/Paks/pakchunk2-Android_ASTC.pak` | 367571213 | `0x15e8b041` | `0x5A6F12E1` | 8 | `0x15c2e850` | 2476000 | `Oodle`, `Zlib` | appears encrypted/unreadable |
| `Analyse/PATCH/AClient/Content/Paks/pakchunk3-Android_ASTC.pak` | 1837582232 | `0x6d8746cc` | `0x5A6F12E1` | 8 | `0x6c30c24b` | 22447216 | `Oodle`, `Zlib` | appears encrypted/unreadable |
| `Analyse/MAIN/AClient/Content/Paks/pakchunk4-Android_ASTC.pak` | 1253209707 | `0x4ab2759f` | `0x5A6F12E1` | 8 | `0x4a56e9ee` | 5999520 | `Oodle`, `Zlib` | appears encrypted/unreadable |

The index regions have high-entropy bytes and no readable mount point/path tokens such as `../../../`, `AClient`, `Content/`, `.uasset`, or `.umap` in the first probed index data. That strongly suggests encrypted PAK indexes, or at least indexes unreadable by a minimal plain-index parser.

The PAK bodies are not globally opaque: raw strings such as Lua/config names and many `Lobby` asset strings are visible. Without a full Unreal parser and the relevant AES/material, per-entry encryption/compression cannot be classified safely.

## launch.pak first pass

Raw strings found in `launch.pak`:

- `Client/Launch/ClientLaunch.lua` at byte offset `2426135`
- `sgaf_config.json` at byte offsets `58684191` and `58746859`
- Several `Lobby` strings, likely UI/client asset strings

No `mgapex.com`, `msdkpass.com`, `apgame.qq.com`, `myqcloud.com`, `game.qq.com`, `RequestAvatarServerList`, `GameServerBackupIpList`, `ServerIP`, `ServerPort`, or game-server endpoint was found in `launch.pak`.

## Network keyword search in all four PAK

Exact network/domain search results:

- No confirmed `mgapex.com` domain strings in the four PAKs.
- No confirmed URLs in the four PAKs.
- Exact backend/code keywords from the requested list were not found, except generic `Lobby`.
- `Lobby` counts: `launch.pak:8`, `pakchunk2:85`, `pakchunk3:1163`, `pakchunk4:356`.
- OBB exact search showed only short `eagw` case-variant hits in compressed/binary data. They do not include `eagw1.mgapex.com` or URL context, so they are treated as false positives/noise.

## Raw file-name strings from PAK bodies

Because indexes appear encrypted/unreadable, this is not a complete file listing. It is only raw visible strings:

| PAK | Interesting visible strings |
| --- | --- |
| `launch.pak` | `Client/Launch/ClientLaunch.lua`, `sgaf_config.json` |
| `pakchunk2-Android_ASTC.pak` | only noisy `.ini/.cfg/.lua` fragments in the first raw extraction; no useful network path |
| `pakchunk3-Android_ASTC.pak` | `Program/Config/MapPickupPointInfo/M1_Can_lootdata.json`, `M1_Can_lootdata_S2.5.json`, `Program/Config/MapPickupPointInfo/M2-Shooting_p.json`, `Program/Config/MapPickupPointInfo/M3_Olympus_lootdata.json`, `Program/Config/MapPickupPointInfo/M2_Desertlands_lootdata.json`, `Client/UMG/SelectLegend/BP_Screen.lua`, `APGameCheatManager.lua`, `Client/UMG/Lobby/Rating/RatingSeasonArchivesLogic.lua` |
| `pakchunk4-Android_ASTC.pak` | mostly noisy extension fragments; no useful network path found |

## Tooling needed for deeper PAK work

To go beyond raw strings, an Unreal PAK-aware tool would be needed, for example `UnrealPak`, FModel, or UE Viewer, plus the correct key/material if the encrypted index needs to be read. No such tool was installed or downloaded.
