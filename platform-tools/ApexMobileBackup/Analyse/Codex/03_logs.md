# Phone log analysis

The `Android-data` and `Phone-storage/data` log trees are duplicates for the files inspected. Main evidence is cited from `Android-data` to avoid duplicate line references.

## Files inspected

| File | Size | Notes |
| --- | ---: | --- |
| `Android-data/cache/GCloudSDKLog/GCloudCore/GCloudCore_2026081217.log` | 17635 | Remote config and GCloudCore plugin startup. |
| `Android-data/cache/GCloudSDKLog/GCloud/GCloud_2026081217.log` | 743939 | GCloud/Puffer/Dolphin update, DNS failures. |
| `Android-data/cache/GCloudSDKLog/GPMSDK/GPMSDK_2026081217.log` | 15539 | Performance/telemetry, speedtest config, local DNS. |
| `Android-data/cache/GCloudSDKLog/MSDK/MSDK_2026081217.xlog` | 822 | Contains mmap text for EA redirect URI. |
| `Android-data/files/TGPA/.tgpacloud` | 226 | Local TGPA config switches, no backend endpoint. |
| `Android-data/cache/GCloud.config` | 162 | Binary/opaque; no cleartext endpoint found. |

## Startup/config sequence

At `2026-08-12 17:21:46`, GCloudCore initializes and starts remote config for MSDK:

- `GCloudCore_2026081217.log:6`: `RemoteConfig.cpp:166|SetUserInfo| module MSDK ... openid <REDACTED>`
- `GCloudCore_2026081217.log:8-9`: `RemoteConfig.cpp:173|Fetch` then `Request config, channel: 6, openID: <REDACTED>, ruleID: 0`
- `GCloudCore_2026081217.log:12`: `UrlRequest::Initialize https://cloudctrl.mgapex.com/cfgpush/getConfig?...` with `did` and `oid` redacted.
- `GCloudCore_2026081217.log:14`: `RemoteConfig url:https://cloudctrl.mgapex.com, maxRetryCount 3, retryCount 3`

The request fails:

- `GCloudCore_2026081217.log:55-58`: `[URLRequest]UnknownHost`, result `3`, `httpStatus:0`, cost `1177 ms`.
- Retries appear at `75-80`, `93-98`, and `111-116`, all ending with `UnknownHost` and `error:3`.

Interpretation: the recent launch did not get past remote config successfully. This explains why later login/lobby/matchmaking traffic is absent from the captured logs.

## GCloud Puffer/resource update

`GCloud_2026081217.log:10-31` dumps Puffer config:

- app version `1.3.672.546`
- resource version `1.3.672.1248`
- `puffer_server` is `puffer.4.707369824.dmp.mgapex.com`
- `res_dir` is the local `UE4Game/AClient/AClient/Saved/Paks/Puffer` path

Runtime DNS activity:

- `GCloud_2026081217.log:59-60`: starts URL info retrieval and RPC init.
- `GCloud_2026081217.log:62`: `ParseHost domain:puffer.4.707369824.dmp.mgapex.com, ... port:0`
- `GCloud_2026081217.log:64-67`: local DNS `result:7`, then `dns failed`.
- `GCloud_2026081217.log:69+`: repeated `CreateLBConnection` address selection failures.

Interpretation: confirmed resource/update endpoint, not game server evidence.

## GCloud Dolphin version/update

`GCloud_2026081217.log:520-529` shows version action config:

- `m_server_url_list` contains `download.2.707369824.dmp.mgapex.com`
- version JSON save path is the app's `Saved/Paks/` directory

Runtime evidence:

- `GCloud_2026081217.log:568`: `m_server_url_list[0][download.2.707369824.dmp.mgapex.com]`
- `GCloud_2026081217.log:588-589`: report key `Version Url` has that same host.
- `GCloud_2026081217.log:592-596`: `NormalConnectVersionSvr` starts, then parses host `download.2.707369824.dmp.mgapex.com`, port `0`.
- Later lines around `4590-4621` report getaddrinfo failure, error stage/code, then cancel action.

Interpretation: confirmed update/version check path; no successful connection.

## GPMSDK/TDM/performance

`GPMSDK_2026081217.log:75-115` shows performance telemetry and level load:

- `reportEvent: gsdk_init`
- level `/Game/Art/Level/Launch/Launch`
- `GPMSDK_2026081217.log:98,105`: `{errno=0, errmsg=success, sip=speedtest.mgapex.com, sport=17000, ... speedlist=1_1.1.1.1:8080;2_1.1.1.1:8081, isCache=1}`
- `GPMSDK_2026081217.log:135`: `ldns: 198.18.53.53`

Interpretation: performance/telemetry path. The `1.1.1.1` host:ports and `198.18.53.53` should not be treated as game servers.

## MSDK xlog

`MSDK_2026081217.xlog` contains mmap text with:

- `EA_REDIRECT_URI = https://eagw1.mgapex.com/web_auth_redirect`

No token/cookie/auth-code value is included in the report.

## Missing from logs

No runtime evidence was found for:

- successful EA/MSDK login/auth exchange
- server list response
- `lobby.mgapex.com`
- matchmaking
- game server connect
- dedicated server IP/port
- reconnect traffic
