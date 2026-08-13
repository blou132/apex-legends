# Server flow reconstruction

This is evidence-based only. `CONFIRMED` means the specific step is shown by a file/log. `PROBABLE` means local code/config names strongly imply the step, but the backend endpoint or successful runtime request was not observed. `UNKNOWN` means the requested link was not demonstrated by the files inspected.

## Flow map

| Stage | Status | Evidence | Interpretation |
| --- | --- | --- | --- |
| App start -> GCloudCore remote config | CONFIRMED | `GCloudCore_2026081217.log:6-15` | GCloudCore initializes, sets MSDK user info, and issues `UrlRequest::Initialize https://cloudctrl.mgapex.com/cfgpush/getConfig?...`. Sensitive user/device fields are redacted. |
| Remote config retry/error | CONFIRMED | `GCloudCore_2026081217.log:55-58`, `75-80`, `93-98`, `111-116` | Requests to `cloudctrl.mgapex.com` fail with `UnknownHost`, `error:3`, `httpStatus:0`. |
| GCloud Puffer init/update | CONFIRMED | `GCloud_2026081217.log:10-31`, `47`, `59-69` | Puffer config names `puffer.4.707369824.dmp.mgapex.com`; DNS resolution fails. This is resource/update plumbing, not a game server. |
| GCloud Dolphin version/update | CONFIRMED | `GCloud_2026081217.log:520-529`, `568`, `588-596`, `4590-4621` | Version/update config names `download.2.707369824.dmp.mgapex.com`; later it reports getaddrinfo failure and cancels the version action. |
| MSDK base endpoint | CONFIRMED in config | `MSDKConfig.ini:11`; `MSDKConfig_Dev.ini:11`; `MSDKConfig_Test.ini:11` | Prod uses `https://itop.mgapex.com`; dev/test use `https://msdk-test.intlgame.com`. |
| MSDK account endpoint | CONFIRMED in config | `MSDKConfig*.ini:47-50` | Stored account URL is `https://cotest.msdkpass.com`; keys/app IDs are present but redacted. Comment also mentions `https://us.msdkpass.com` as formal. |
| EA login/auth | CONFIRMED in config | `MSDKConfig.ini:63-64`, dev/test equivalents | Prod EA auth URL is `accounts.ea.com`; prod redirect is `https://eagw1.mgapex.com/web_auth_redirect`. Dev/test use `accounts.int.ea.com` and `test.apgame.qq.com:7000`. Runtime log does not show a successful login request. |
| Apex `LoginMgr` code path | CONFIRMED as symbol/source path | `libUE4-strings.txt:30196`, `40303`, `65946`; `libUE4.so` contains `LoginMgr`/`ULoginMgrWrapper` | The client contains login manager code, but static strings do not expose the concrete lobby/server-list endpoint. |
| Avatar/server list request | PROBABLE code path, endpoint unknown | `RequestAvatarServerList` at `libUE4-strings.txt:29666`; `EVENTID_AVATARSERVERLIST_RETURN` at `20093`; `OpenServerList` at `51911`; `ServerListName` at `9930` | Strong evidence of a server-list UI/event path. No URL, host, IP, or port is tied to this path in the available strings/logs. |
| Lobby | CONFIRMED as client/game concept, network endpoint unknown | Many `Lobby` strings; `ApexLobbyPlayerController.cpp` at `libUE4-strings.txt:9516` | No `lobby.mgapex.com` occurrence was found. Do not classify any discovered address as lobby backend without more evidence. |
| Matchmaking | UNKNOWN | Exact `Matchmaking/MatchMaking` endpoint evidence not found in the targeted string outputs | No concrete matchmaking host or protocol was identified. |
| Game server / dedicated server | PROBABLE code path, endpoint unknown | `GameServerBackupIpList` at `libUE4-strings.txt:20648`; `SyncPayloadToGameServer` at `41538`; `UEDSToolkit` at `19296`; `DSControllerComponent.cpp` at `29114`; `socket_http.cpp` at `44849`; `DedicatedServer` flags in `libUE4-strings.txt` and `libUE4.so` | The client contains UE dedicated-server/reconnect/network support. No concrete game server IP/port was found. |
| Reconnection | CONFIRMED as code path, endpoint unknown | `OnServerAboutToReconnect` at `36306`; `OnPreReconnectOnServer` at `41077`; `ClientNotifyReconnectedSuccessfully` at `21611`; `ReconnectSyncData` at `13549` | Reconnect logic exists in client symbols, but no runtime reconnect traffic is present in logs. |

## Conservative architecture

```
Client
  -> GCloudCore RemoteConfig (CONFIRMED: cloudctrl.mgapex.com, runtime request failed DNS)
  -> GCloud Puffer/Dolphin update/version (CONFIRMED: puffer.* and download.* dmp.mgapex.com, DNS failed)
  -> MSDK / EA login config (CONFIRMED in APK config, no successful runtime login observed)
  -> Apex LoginMgr (CONFIRMED symbol/source path)
  -> RequestAvatarServerList / OpenServerList (PROBABLE internal step, backend UNKNOWN)
  -> Lobby (CONFIRMED client concept, endpoint UNKNOWN)
  -> Matchmaking (UNKNOWN)
  -> UE dedicated/game server (PROBABLE client capability, endpoint UNKNOWN)
  -> Reconnection (CONFIRMED client code path, endpoint UNKNOWN)
```

## Important negatives

- `lobby.mgapex.com` was not found.
- No concrete game server IP/port was found.
- `GameServerBackupIpList` is only a symbol/string, not a populated address list in the scanned evidence.
- Runtime logs stopped at config/update DNS failures and do not show login completion, server list retrieval, lobby entry, matchmaking, game-server connect, or reconnect.
- The discovered `puffer.*`, `download.*`, `speedtest.*`, TDM, Bugly, Pandora, payment, and voice endpoints should not be classified as game servers.
