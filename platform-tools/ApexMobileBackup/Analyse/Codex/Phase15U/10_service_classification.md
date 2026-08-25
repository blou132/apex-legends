# Service classification

| Component/host | Classification | Evidence boundary |
| --- | --- | --- |
| TDM / `tdm.mgapex.com` | `TELEMETRY` | Explicit repeated HTTP POST reporting route; curl code `6`. Not an update gate. |
| GCloudCore / `cloudctrl.mgapex.com` | `REMOTE_CONFIG` | Explicit `RemoteConfig` request and `UnknownHost` retries. |
| TGPA / `tgpa.mgapex.com` | `REMOTE_CONFIG` | Explicit cloud-control URL and offline DNS failure. |
| GCloud Puffer / `puffer.4.707369824.dmp.mgapex.com` | `VERSION_UPDATE` | PAK update configuration plus `MakeSureGetUrlFromServer`, DNS failure, timeout, and retry. |
| MSDK / `itop.mgapex.com` | `CONFIGURED_ONLY` | Default URL was read; no direct request is promoted from this run. |
| GVoice hosts | `CONFIGURED_ONLY` | Endpoint configuration was logged; no direct request is promoted. |
| Login service | `UNKNOWN` | No new request or named stage. |
| Avatar/server-list service | `UNKNOWN` | No new request or named stage. |
| Game server | `UNKNOWN` | No new request or named stage. |

TDM, remote config, update, login, server-list, and game-server roles remain
separate. The Puffer attempt is an update-service witness, but the exact
`I54140714` producer is not assigned without a direct edge.
