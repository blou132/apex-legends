# Network timeline

All operations below come from the existing deliberately offline Phase15D run.
Times are relative to Apex process creation. Hosts are published without query
strings, headers, bodies, tokens, or identifiers.

## Apex-attributed operations

| Component | Relative request times | Method / host | Result |
| --- | --- | --- | --- |
| TDM | +11.278, +14.103, +24.310, +39.382, +59.419 s | POST `tdm.mgapex.com` | Each attempt ends with curl code 6 at DNS; HTTP status remains 0. |
| GCloud RemoteConfig | +12.069, +12.307, +22.624, +32.981, +43.574 s | GET `cloudctrl.mgapex.com` | `UnknownHost`; no HTTP response. |
| MSDK | Error callback at +12.248 s | No independent request proven | Receives the GCloud configuration failure; separately reads built-in defaults. |
| GVoice | Configuration values at about +12.3 s | No launched request proven | Endpoint setup strings only; not promoted to network operations. |

The first application request remains the TDM POST at `+11.278 s`. The process
continues through +60 seconds and emits background GC at +61.066 seconds, so
TDM failure does not terminate the observed startup/process lifetime. This does
not prove that TDM has no effect on later gameplay.

## Process attribution exclusions

- Finsky/Google Play logs come from separate processes. An `Internet connection
  lost` message is a network-state observation, not an Apex request.
- No PlayCommon request is present in the captured window.
- Keyboard and Facebook documentation/configuration URLs are not evidence of a
  launched Apex transport operation.
- A Google Play SSL shutdown message belongs to a different process and is not
  attributed to Apex.

## GCloud correlation

The log reports the remote-config cache absent at `+11.029 s`, explicit reads
from built-in defaults, two immediate requests, and later retries with the
remaining retry count decreasing to zero. This agrees with Phase10.

```text
GCLOUD_RUNTIME_RETRY_OBSERVED = CONFIRMED YES
GCLOUD_RUNTIME_DEFAULT_USE_EVIDENCE = CONFIRMED YES
GCLOUD_GAMEPLAY_GATE = UNKNOWN
TDM_FAILURE_TERMINATES_OBSERVED_PROCESS = CONFIRMED NO
```
