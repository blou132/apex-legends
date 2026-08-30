# Network isolation

Before installation, airplane mode was enabled, Wi-Fi and mobile data were
disabled, IPv4 and IPv6 route tables were empty, and Android reported no active
default network. The same checks passed during warm-up, immediately before the
active trace, and after cleanup.

The stock environment did not require experimental per-UID firewall work. The
whole device had no usable external route. Observed DNS failures are local
application evidence and do not establish an outbound connection.

```text
AIRPLANE_MODE = ENABLED
WIFI_DISABLED = YES
MOBILE_DATA_DISABLED = YES
INTERNET_ROUTE_PRESENT = NO
APEX_UID_NETWORK_BARRIER = UNAVAILABLE_NOT_REQUIRED_WHOLE_DEVICE_OFFLINE
NETWORK_ISOLATION_GATE = PASS
```
