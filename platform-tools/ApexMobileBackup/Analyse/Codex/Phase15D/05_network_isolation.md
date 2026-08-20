# Guest network isolation

The initial guest state was recorded before any change:

```text
AIRPLANE_MODE = 0
WIFI = 1
MOBILE_DATA = 1
ACTIVE_DEFAULT_NETWORK = PRESENT
```

Before Apex launch, standard Android controls enabled airplane mode and disabled
Wi-Fi and mobile data. Read-only checks then showed:

```text
AIRPLANE_MODE = 1
WIFI = 0
MOBILE_DATA = 0
ACTIVE_DEFAULT_NETWORK = NONE
DEFAULT_ROUTE = NONE
NETWORK_ISOLATION_CONFIRMED = YES
```

No ping, curl, external test packet, host-network change, DNS redirection, or
local backend was used. Runtime requests therefore failed without receiving a
response. The first observed application request was a TDM HTTP POST to
`tdm.mgapex.com`; it failed at DNS resolution with curl result code `6`.
