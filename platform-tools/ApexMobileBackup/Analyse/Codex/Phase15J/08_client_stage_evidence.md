# Client-stage evidence

| Client stage | Official files | Logcat | Result |
| --- | --- | --- | --- |
| Lua | No hit | No hit | `NO_NEW_EVIDENCE` |
| ClientLaunch | No hit | No hit | `NO_NEW_EVIDENCE` |
| EventSystem/PostCppEvent | No hit | No hit | `NO_NEW_EVIDENCE` |
| Login/LoginMgr | Generic MSDK provider text only | No client-stage hit | `NO_NEW_EVIDENCE` |
| RequestAvatarServerList | No hit | No hit | `NO_NEW_EVIDENCE` |
| Server-list event `0x138` | No hit | No hit | `NO_NEW_EVIDENCE` |

Absence from these sinks does not prove that a stage was not reached. It only
means Phase15J adds no runtime witness for it.

```text
LUA_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
CLIENTLAUNCH_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
EVENTSYSTEM_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
LOGIN_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
REQUEST_AVATAR_SERVER_LIST_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
SERVER_LIST_EVENT_0X138_RUNTIME_EVIDENCE = NO_NEW_EVIDENCE
```
