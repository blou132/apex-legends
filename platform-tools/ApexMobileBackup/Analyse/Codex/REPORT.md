# Apex Legends Mobile backend reverse-engineering inventory

## 1. Decouvertes les plus importantes

- Runtime logs confirm the startup path reaches `GCloudCore` remote config at `https://cloudctrl.mgapex.com/cfgpush/getConfig?...`, but DNS fails with `UnknownHost`.
- GCloud update/resource paths are confirmed: `puffer.4.707369824.dmp.mgapex.com` and `download.2.707369824.dmp.mgapex.com`. Both fail DNS in the captured launch.
- APK configs confirm MSDK/EA login endpoints and redirects, including prod `https://itop.mgapex.com`, EA auth at `accounts.ea.com`, and redirect `https://eagw1.mgapex.com/web_auth_redirect`.
- `libUE4` contains strong server-flow symbols: `LoginMgr`, `RequestAvatarServerList`, `EVENTID_AVATARSERVERLIST_RETURN`, `OpenServerList`, `GameServerBackupIpList`, `SyncPayloadToGameServer`, `UEDSToolkit`, `DSControllerComponent`, `socket_http`, and reconnect callbacks.
- No concrete lobby, matchmaking, or game server endpoint was found.

## 2. Domaines/endpoints confirmes

Confirmed config/update: `cloudctrl.mgapex.com`, `puffer.4.707369824.dmp.mgapex.com`, `download.2.707369824.dmp.mgapex.com`, `apgame-client-1258344700.cos.ap-shanghai.myqcloud.com/ConfigHotFix`.

Confirmed auth/login/config: `itop.mgapex.com`, `msdk-test.intlgame.com`, `cotest.msdkpass.com`, `accounts.ea.com`, `accounts.int.ea.com`, `eagw1.mgapex.com`, `test.apgame.qq.com:7000`.

Confirmed telemetry/support/payment/voice: `tdm.mgapex.com:8013`, `speedtest.mgapex.com:17000`, `bugly-android.mgapex.com`, `bugly-ios.mgapex.com`, `pandora.mgapex.com`, `pdrlog.game.qq.com`, `ire.csoversea.mgapex.com`, `pay.mgapex.com`, Centauri domains, and GCloudVoice `gcloudsdk.com`/`gcloudcs.com` endpoints.

Not found: `lobby.mgapex.com`, `file.mgapex.com`, `voice-sg.mgapex.com`, `tgpa.mgapex.com`.

## 3. IP/ports trouves

- Payment/test IPs: `101.32.133.41`, `183.61.41.148`.
- Telemetry/performance values: `speedtest.mgapex.com` port `17000`, `1.1.1.1:8080`, `1.1.1.1:8081`, local DNS `198.18.53.53`.
- Configured service ports: `tdm.mgapex.com:8013`, `test.apgame.qq.com:7000`, GCloudVoice `443`, `10001/udp`, `18301/udp`, `8011/udp`.
- Placeholder/private strings: `0.0.0.0`, `127.0.0.1:1000`, `10.0.0.1`, `10.0.0.108`.

No IP/port is proven to be a game server.

## 4. Auth/login

`MSDKConfig.ini` confirms prod EA auth and redirect:

- `EA_LOGIN_URL = https://accounts.ea.com/connect/auth?...client_id=<REDACTED>...`
- `EA_REDIRECT_URI = https://eagw1.mgapex.com/web_auth_redirect`

Dev/test use `accounts.int.ea.com` and `test.apgame.qq.com:7000/apgame_account_auth`. MSDK SDK key/account app ID fields are present but redacted. Runtime logs do not show a successful login/auth exchange.

## 5. Server list

Symbols confirm a likely server-list path:

- `ServerListName` at `libUE4-strings.txt:9930`
- `EVENTID_AVATARSERVERLIST_RETURN` at `libUE4-strings.txt:20093`
- `RequestAvatarServerList` at `libUE4-strings.txt:29666`
- `OpenServerList` at `libUE4-strings.txt:51911`

No URL, IP, port, or runtime response was tied to this path.

## 6. Lobby/matchmaking

Lobby is heavily present as a client/game concept, including `ApexLobbyPlayerController.cpp`. No `lobby.mgapex.com` was found. No concrete matchmaking endpoint or successful matchmaking log was found.

## 7. Game server / Dedicated Server

`libUE4` strings confirm dedicated-server/game-server related client code:

- `GameServerBackupIpList`
- `SyncPayloadToGameServer`
- `UEDSToolkit`
- `DSControllerComponent.cpp`
- `socket_http.cpp`
- `OnServerAboutToReconnect`, `OnPreReconnectOnServer`, `ClientNotifyReconnectedSuccessfully`

These are code-path indicators only. They do not expose a concrete game server host, IP, port, protocol, or server list.

## 8. Ce qui manque encore

- successful remote config payload
- successful MSDK/EA login traffic
- avatar/server list response
- lobby host
- matchmaking host
- game server IP/port
- readable PAK index or extracted network config from PAKs

## 9. Prochaine etape la plus utile

Capture a fresh launch with working DNS/network and preserve logs around the first successful `cloudctrl.mgapex.com` response. That is the earliest confirmed blocker in the current evidence, and it likely gates the later login/server-list/lobby path.

Reports created:

- `ApexMobileBackup/Analyse/Codex/01_endpoints.md`
- `ApexMobileBackup/Analyse/Codex/02_server_flow.md`
- `ApexMobileBackup/Analyse/Codex/03_logs.md`
- `ApexMobileBackup/Analyse/Codex/04_pak_analysis.md`
- `ApexMobileBackup/Analyse/Codex/05_next_steps.md`
