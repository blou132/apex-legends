# First connection

## Observed boundary

```text
FIRST_BACKEND_HOST = tdm.mgapex.com
DESTINATION_PORT = 8013
PROTOCOL = HTTPS_CONFIGURED
TRANSPORT = UNKNOWN_NOT_CONNECTED
DNS_RESOLVED = NO
CONNECTION_OBSERVED = NO
TLS_ATTEMPT = NO
```

The destination port and HTTPS scheme are explicit in the URL passed to TDM's curl HTTP layer. The request fails with libcurl code `6` before a destination address exists.

Consequently:

- TCP is not promoted from convention to runtime fact;
- no SYN, accepted local connection, TLS ClientHello, SNI, TLS version, certificate check, or application response was observed;
- the TLS certificate boundary was not reached.

No raw packet capture exists because no connection occurred and no second run was performed.
