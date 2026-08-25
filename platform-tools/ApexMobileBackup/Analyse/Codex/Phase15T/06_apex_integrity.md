# Apex integrity

The final Package Manager audit confirms:

```text
PACKAGE = com.ea.gp.apexlegendsmobilefps
VERSION_NAME = 1.3.672.546
VERSION_CODE = 64003140
PRIMARY_CPU_ABI = arm64-v8a
USER_0_INSTALLED = YES
PROCESS_RUNNING = NO
```

The public OBB directory still contains exactly:

| File | Exact size | Device-local timestamp |
| --- | ---: | --- |
| `main.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1,942,013,346 bytes | 2026-08-13 17:37 |
| `patch.64003140.com.ea.gp.apexlegendsmobilefps.obb` | 1,837,582,506 bytes | 2026-08-13 17:38 |

These names and sizes remain identical to the authoritative Phase15S state.
Apex was not launched, uninstalled, reinstalled, force-stopped, cleared, or
modified. Its data, cache, APK, and OBB files were never cleanup targets.
