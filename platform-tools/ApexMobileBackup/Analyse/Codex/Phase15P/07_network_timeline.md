# Network timeline

The SystemUI hard stop occurred before guest network-state recording and
isolation. No guest network setting was changed, no client was launched, and no
request was emitted or answered.

```text
NETWORK_CHANGED = NO
NETWORK_ISOLATION = NOT_REACHED
CLIENT_NETWORK_ATTEMPTS = NONE_CLIENT_NOT_LAUNCHED
NETWORK_RESTORED = NOT_REQUIRED_NETWORK_UNCHANGED
```
