# Evidence table

| Claim | Evidence class | Result |
|---|---|---|
| Correct target | Read-only ADB and USB identity | PRA-LX1 / C33 / hi6250 confirmed |
| Inputs intact | Local SHA-256 rehash | All pinned inputs matched |
| Board match | Current photos vs three archived references | Confirmed |
| Exact testpoint | Physical contact plus bootrom enumeration | Confirmed |
| Bootrom | Windows PnP, sanitized | Huawei USB COM detected |
| VCOM driver | Signed INF/catalog and live binding | Version 2.0.7.1 active |
| PotatoNV profile | Upstream table and local manifest | Kirin 65x (A) |
| FBLOCK | PotatoNV UI state | Unchecked |
| PotatoNV outcome | Private raw log, summarized only | RAM upload then timeout |
| Temporary fastboot issue | Windows PnP problem state | Missing driver during attempt |
| Unlock code | Control-flow and private log review | Not generated |
| Destructive unlock | Command audit | Not executed |
| Stock recovery | Normal Huawei USB and ADB after stop | Android returned normally |
| Samsung isolation | USB/ADB audit | No Samsung endpoint |

Raw photographs, raw logs, binaries, firmware, application packages, and
unique device identifiers are intentionally excluded from Git.
