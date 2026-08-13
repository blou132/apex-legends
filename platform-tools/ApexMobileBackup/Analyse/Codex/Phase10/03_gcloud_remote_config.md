# GCloud remote config

## Construction and start

`GCloud::ConfigureImpl` in `libgcloudcore.so` owns the remote-config flow:

```text
_Init                 0x186a14
Request               0x186b14
OnDataTaskFinished    0x1863ac
_RetryRequest         0x185544
_ParseJsonObject      0x185774
StartOnce             0x186f54
ConfigureImpl         0x18717c
```

`_Init` obtains `RemoteConfigUrl` from the default/local bundle when no explicit URL is set. `StartOnce` reads `RemoteConfigMaxRetryCount` with default `3`. `Request` builds the documented `/cfgpush/getConfig` GET and starts a URL data task.

## Valid response

Network result `0` enters JSON parsing. A valid object requires a successful return indicator and contains `biz_data`, including `rule_id` and `merged_cfg`. Valid merged configuration updates the bundle and invokes module observers through `OnConfigureRefreshed`.

The exact values required by gameplay or Login are not established.

## Local storage

The constructor creates a `RemoteConfig.config` cache bundle. The Phase9C run explicitly reports that this file does not exist. `ConfigFile::GetFromCache` overloads return the caller-provided default when a key or bundle is unavailable.

```text
GCLOUD_CACHE_MECHANISM = CONFIRMED
GCLOUD_CALLER_DEFAULT_MECHANISM = CONFIRMED
GCLOUD_COMPLETE_OFFLINE_CONFIG = UNKNOWN
```
