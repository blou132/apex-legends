# Network boundary

## Controlled run

The initial radio settings were recorded before launch. Mobile data was
disabled and airplane mode was used because the device ignored the direct
Wi-Fi disable request. Before launch, Android reported Wi-Fi disabled and no
active default network.

The client was force-stopped before launch and again immediately after the
failed diagnostic request. The final settings exactly matched the recorded
initial state:

| Setting | Initial | During run | Final |
| --- | ---: | ---: | ---: |
| Airplane mode | 0 | 1 | 0 |
| Wi-Fi setting | 1 | disabled by airplane mode | 1 |
| Mobile data | 1 | 0 | 1 |

Post-run verification confirmed Wi-Fi reconnected, mobile data restored, and
no Apex process running.

```text
NETWORK_OFFLINE_CONFIRMED = YES
NETWORK_RESTORED = YES
DEBUG_PROPERTY_CLEARED = NOT_USED
```
