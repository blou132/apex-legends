# Network isolation

The guest network state was recorded before isolation. Apex started only after
airplane mode was enabled, Wi-Fi and mobile data were disabled, Android
reported no active default network, and the route table contained no default
route.

No connectivity test was sent. The guest stayed isolated for the complete
launch and observation. Logged request attempts therefore received no local or
external response.

```text
INITIAL_AIRPLANE_MODE = OFF
INITIAL_WIFI = ON
INITIAL_MOBILE_DATA = ON
ISOLATED_AIRPLANE_MODE = ON
ISOLATED_WIFI = OFF
ISOLATED_MOBILE_DATA = OFF
NO_ACTIVE_DEFAULT_NETWORK = YES
NO_DEFAULT_ROUTE = YES
NETWORK_ISOLATION_CONFIRMED = YES
```
