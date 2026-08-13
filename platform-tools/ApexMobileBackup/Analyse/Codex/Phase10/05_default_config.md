# Default and cached configuration

## Confirmed mechanisms

`libgcloudcore.so` provides:

- a default configuration bundle;
- a persistent `RemoteConfig.config` cache;
- typed `GetFromCache` methods;
- caller-provided fallback values when a key is absent;
- default retry count `3` and retry timer `10,000 ms` for RemoteConfig.

The current run had no RemoteConfig cache. Therefore cache support is not evidence that useful cached data was available.

## RPC-related values

`libgcloud.so` function `0x4d35d0` reads each key through the GCloud RemoteConfig accessor and supplies an explicit fallback argument. Earlier local runtime evidence records the same effective values:

| Key | Observed value | Source classification |
| --- | --- | --- |
| `RpcConnectTimeout` | `15` | `DEFAULT` |
| `RpcRetryIncrement` | `7` | `DEFAULT` |
| `RpcAddressSvrPortList` | `8085|8080` | `DEFAULT` |
| `RpcAddressSvrBkIPList` | empty | `DEFAULT` |
| `RpcConnectMode` | `0` | `DEFAULT` |
| `RpcConnectTdrProto` | `100` | `DEFAULT` |
| `RpcParallelChannels` | `15` | `DEFAULT` |
| `RpcParallelCount` | `1` | `DEFAULT` |

These values are local fallback arguments, not values proven to originate from the `/getConfig` response. In particular, no usable address-server hostname is established, and the backup-IP list defaults to empty. The confirmed defaults are therefore insufficient to prove a complete offline route to Login.

```text
GCLOUD_FALLBACK = CONFIRMED CACHE_AND_CALLER_DEFAULT_MECHANISMS
DEFAULT_CONFIG_SUFFICIENT_FOR_LOGIN = UNKNOWN
```
