# Device and integrity preflight

Exactly one physical Android target was present. Its non-sensitive identity
matched the authorized lab phone.

| Property | Result |
| --- | --- |
| Model | `PRA-LX1` |
| Android | `8.0.0` / API `26` |
| Primary ABI | `arm64-v8a` |
| Apex version | `1.3.672.546` (`64003140`) |
| Apex primary ABI | `arm64-v8a` |
| Apex process before launch | stopped |

Both production expansion files matched their expected exact names and sizes:

| Expansion | Bytes |
| --- | ---: |
| `main.64003140.com.ea.gp.apexlegendsmobilefps.obb` | `1942013346` |
| `patch.64003140.com.ea.gp.apexlegendsmobilefps.obb` | `1837582506` |

No multi-gigabyte rehash was repeated because neither name nor size differed.

The initial Wi-Fi and mobile-data states were both enabled, with an active
default network and default route. Huawei's `svc wifi disable` path did not
complete normally, so the run used the supported Android airplane-mode
setting/broadcast together with mobile-data disable. Before launch, Wi-Fi was
disabled, mobile data was disabled, airplane mode was enabled, no active
default network existed, and no default route existed.

```text
APEX_PACKAGE_INTACT = YES
APEX_OBBS_INTACT = YES
NETWORK_ISOLATED = YES
NO_ACTIVE_DEFAULT_NETWORK = YES
NO_DEFAULT_ROUTE = YES
```
