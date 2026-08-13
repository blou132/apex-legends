# Backend sequence

## Confirmed runtime order

| Order | Relative time | Component | Operation | Result |
| ---: | ---: | --- | --- | --- |
| 1 | `+1.727 s` | TDM | `POST https://tdm.mgapex.com:8013/tdm/v1/route` | libcurl `6` at `+1.742 s` |
| 2 | `+1.958 s` | GCloudCore | `GET https://cloudctrl.mgapex.com/cfgpush/getConfig` | `UnknownHost` at `+1.969 s` |
| 3 | `+2.084 s` | GCloudCore | retry of the initialized request | `UnknownHost` at `+2.088 s` |
| 4 | `+2.352 s` | TDM | retry of route POST | libcurl `6` at `+2.366 s` |
| 5 | `+2.723 s` | PlayCommon | HTTPS timestamp request to `play.googleapis.com` | explicit `UnknownHostException` at `+2.731 s` |

Later retries repeat TDM and GCloudCore failures. They do not introduce a newly proven bootstrap dependency before the first request.

## Configuration-only values not promoted

- `itop.mgapex.com`: MSDK default URL read, no request-start evidence in the captured run.
- GVoice host list: configuration initialization, no corresponding connection evidence in the captured run.
- Facebook documentation URLs: diagnostic references, not runtime destinations.

```text
DNS_REQUEST_1 = tdm.mgapex.com, inferred from TDM POST plus libcurl code 6
DNS_REQUEST_2 = cloudctrl.mgapex.com, correlated request plus UnknownHost
DNS_REQUEST_3 = play.googleapis.com, explicit UnknownHostException
```
