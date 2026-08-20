# Network isolation

The initial guest settings were recorded before any change:

```text
airplane_mode_on = 0
wifi_on = 1
mobile_data = 1
```

Before Apex launch, the controlled state was:

```text
airplane_mode_on = 1
wifi_on = 0
mobile_data = 0
active_default_network = none
default_route = none
```

No ping, curl, DNS test, backend request, or traffic interception was used.
The settings remained isolated throughout the single Apex observation. They
were restored exactly after force-stop.

```text
NETWORK_ISOLATION_CONFIRMED = YES
```
