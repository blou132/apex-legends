# Frida server failure

Bounded current-run evidence identifies a null-pointer SIGSEGV in the Frida
17.17.0 server while ART/helper initialization was active. The process exited
with status 139. Two independent supervised starts reproduced the same class
of failure.

This is not a client/server version mismatch: host core, server version, and
deployed hash matched. It is also not an attach transport failure in this run,
because the server died before a client attach request.

```text
FRIDA_SERVER_FAILURE_CLASS = SIGSEGV_NULL_DEREFERENCE_DURING_ART_HELPER_INITIALIZATION
FRIDA_SERVER_EXIT_CODE = 139
FRIDA_TRANSPORT_CAUSAL_EVIDENCE = NO_SERVER_CRASHED_BEFORE_CLIENT_ATTACH
```
