# Client startup

## Result

```text
CLIENT_STARTED = YES
LIBUE4_LOADED = NOT_CONFIRMED
FIRST_FAILURE = DNS_BACKEND_UNAVAILABLE_UNDER_NETWORK_BLOCK
FINAL_GATE = B HUAWEI_CLIENT_STARTS_THEN_BACKEND_FAILURE
```

## Observations

- Android started `com.epicgames.ue4.GameActivity` for the Apex package.
- The application process and activity remained present through every check over more than 90 seconds.
- The log contains UE4-tagged splash and target-SDK messages.
- No fatal signal, `FATAL EXCEPTION`, `SIGSEGV`, `SIGABRT`, `UnsatisfiedLinkError`, or no-matching-ABI error occurred.
- The screen remained black in the local-only capture.
- A hostname resolution failure appeared while the Huawei was intentionally offline.

## Native-library limit

The unprivileged shell could not read `/proc/<pid>/maps`. A global `lsof` view exposed only four descriptors for the process and no mapped shared objects. These observations do not prove that `libUE4.so` was absent, so its load state remains `NOT_CONFIRMED`.

The application was force-stopped after observation. Huawei network settings were restored to their initial values: airplane mode off, Wi-Fi on, and mobile data on.
