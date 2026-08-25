# Service boundary

Phase15V preserves the established role separation:

| Component | Classification | Phase15V change |
| --- | --- | --- |
| TDM | `TELEMETRY` | none |
| GCloudCore | `REMOTE_CONFIG` | none |
| TGPA | `REMOTE_CONFIG` | none |
| GCloud Puffer | `VERSION_UPDATE` | native owner and bounded failure chain resolved |
| Login | `UNKNOWN` | no direct edge from update |
| Avatar/server-list | `UNKNOWN` | no direct edge from update |
| Game server | `UNKNOWN` | no direct edge from update |

Puffer's `GetUpdateInfo` RPC is not login and must not be treated as a game or
server-list response. No Lua, `ClientLaunch`, Login, LoginMgr, or event `0x138`
edge is present in the resolved update chain.

```text
LUA_DIRECT_EDGE_FROM_UPDATE = NO
LOGIN_DIRECT_EDGE_FROM_UPDATE = NO
```
