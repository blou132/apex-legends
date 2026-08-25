# Offline client network timeline

All entries below are client-generated attempts observed after isolation. No
response was sent and no DNS redirection, proxy, backend, or endpoint emulation
was used. Relative times are rounded from the `GameActivity` start.

| Relative time | Component | Method | Host | Failure | Class |
| ---: | --- | --- | --- | --- | --- |
| about `+3 s` | TDM | `POST` | `tdm.mgapex.com` | curl code `6`, no HTTP response | `TELEMETRY` |
| about `+3 s` | GCloudCore | not logged | `cloudctrl.mgapex.com` | `UnknownHost` | `REMOTE_CONFIG` |
| about `+14/+24/+34 s` | GCloudCore retries | not logged | `cloudctrl.mgapex.com` | `UnknownHost` | `REMOTE_CONFIG` |
| through about `+109 s` | TDM retries | `POST` | `tdm.mgapex.com` | curl code `6` | `TELEMETRY` |
| about `+116 s` | TGPA cloud control | not logged | `tgpa.mgapex.com` | `UnknownHost` / network unreachable | `REMOTE_CONFIG` |
| about `+117 s` | GCloud Puffer | RPC method unknown | `puffer.4.707369824.dmp.mgapex.com` | DNS failure, `NetworkNotReachable`, then connection timeout | `VERSION_UPDATE` |
| about `+159 s` | GCloud Puffer | RPC method unknown | same host | explicit get-URL failure; retry begins | `VERSION_UPDATE` |

Configured MSDK and voice-service URLs are not promoted to attempts unless a
request/failure was directly logged. No login, avatar/server-list, or game
server request is confirmed.

The update modal appears during the Puffer update interval, but timing alone is
not used to assign the modal's exact error code to Puffer.
