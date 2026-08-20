# Network timeline

The guest remained strictly offline. Only cleaned method, host, component,
relative-time, and failure-layer metadata is retained.

## Before post-resume T0

| Relative time | Component | Method and host | Result |
| ---: | --- | --- | --- |
| `-3.788 s` | TDM | `POST tdm.mgapex.com` | name resolution failed |
| `-3.119 s` | GCloud RemoteConfig | `GET cloudctrl.mgapex.com` | name resolution failed |
| `-0.804 s` | TDM | `POST tdm.mgapex.com` | name resolution failed |

## After post-resume T0

| Relative time | Component | Method and host | Result |
| ---: | --- | --- | --- |
| `+0.109 s` | TDM | `POST tdm.mgapex.com` | name resolution failed at `+0.200 s` |
| `+7.726 s` | GCloud RemoteConfig | `GET cloudctrl.mgapex.com` | name resolution failed |
| `+15.246 s` | TDM | `POST tdm.mgapex.com` | name resolution failed |
| `+18.130 s` | GCloud RemoteConfig | `GET cloudctrl.mgapex.com` | name resolution failed |
| `+28.271 s` | GCloud RemoteConfig | `GET cloudctrl.mgapex.com` | name resolution failed |
| `+35.338 s` through `+260.479 s` | TDM | periodic `POST tdm.mgapex.com` retries | name resolution failed |

The first post-resume request repeats the known TDM route operation. No new
host appears only after OBB validation, no response is received, and the
process remains alive. These attempts do not prove a Login or server-list
dependency.

```text
FIRST_POST_RESUME_NETWORK_ATTEMPT = +0.109S_TDM_POST_TDM.MGAPEX.COM
FIRST_POST_RESUME_FAILURE_LAYER = NAME_RESOLUTION
NEW_POST_RESUME_ENDPOINT = NO
NETWORK_RESPONSE_RECEIVED = NO
```
