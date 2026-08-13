# GCloud failure path

## Error classes

`OnDataTaskFinished` distinguishes transport failure from a network-success response:

- DNS/network failure: nonzero task result, observed as `UnknownHost`, result `3`, HTTP status `0`, empty data.
- HTTP non-200: represented by the request layer as an unsuccessful task/result; exact per-status mapping is not fully recovered.
- Empty or malformed response after network success: JSON parse/validation fails.
- Valid response: merged configuration is stored and observers are notified.

On network error, state changes to an error state and `_RetryRequest` runs. While retry count is positive it decrements the count and starts a one-shot 10-second timer. At zero, no further retry or success observer is issued.

## Startup effect

In the run, `GCloudCoreInnerPlugin::OnStartup`, service registration, worker creation, and later post-startup/lifecycle work continue despite the first failure. Four total fetch attempts are observed: the initial request and three timed retries.

```text
GCLOUD_CONFIG_OPTIONAL = CONFIRMED for observed SDK/plugin startup
GCLOUD_CONFIG_REQUIRED = UNKNOWN for later gameplay/Login configuration
GCLOUD_STARTUP_GATE = CONFIRMED NO_FOR_OBSERVED_PLUGIN_STARTUP
```

The failure path does not itself invoke `OnConfigureRefreshed`. A later consumer that requires a missing key remains possible but is not identified by current evidence.
