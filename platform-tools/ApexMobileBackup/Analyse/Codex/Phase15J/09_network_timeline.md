# Network timeline

The guest had no active default network and no default route. The client still
issued its normal offline attempts; nothing answered them.

Times are relative to post-resume T0.

| Relative time | Component | Method | Host | Failure layer |
| ---: | --- | --- | --- | --- |
| +0.060 s | TDM | POST | `tdm.mgapex.com:8013` | Name resolution, client code 6 |
| +7.789 s | GCloudCore | Config request | `cloudctrl.mgapex.com` | UnknownHost |
| +15.098 s | TDM | POST | `tdm.mgapex.com:8013` | Name resolution, client code 6 |
| +18.040 s | GCloudCore | Config retry | same host | UnknownHost |
| +28.185 s | GCloudCore | Config retry | same host | UnknownHost |
| +35.114 s | TDM | POST | same host | Name resolution, client code 6 |
| +60.129 s | TDM | POST | same host | Name resolution, client code 6 |
| +90.143 s | TDM | POST | same host | Name resolution, client code 6 |
| +125.155 s | TDM | POST | same host | Name resolution, client code 6 |
| +165.164 s | TDM | POST | same host | Name resolution, client code 6 |

GCloud retries stop after the early window; TDM continues on increasing
intervals while Apex remains alive. No response, HTTP status, redirect, proxy,
or backend was supplied. Temporal correlation still does not establish a splash
dependency.
